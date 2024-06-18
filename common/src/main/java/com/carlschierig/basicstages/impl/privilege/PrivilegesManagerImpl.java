package com.carlschierig.basicstages.impl.privilege;

import com.carlschierig.basicstages.api.privilege.Privilege;
import com.carlschierig.basicstages.api.privilege.PrivilegeMap;
import com.carlschierig.basicstages.api.privilege.PrivilegeType;
import com.carlschierig.basicstages.api.privilege.PrivilegesManager;
import com.carlschierig.basicstages.api.registry.BSRegistries;
import com.carlschierig.basicstages.impl.network.S2CPackets;
import com.carlschierig.basicstages.impl.util.BSUtil;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.mojang.serialization.Codec;
import com.mojang.serialization.JsonOps;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.ResourceManagerReloadListener;
import net.minecraft.world.entity.player.Player;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PrivilegesManagerImpl extends PrivilegesManager implements ResourceManagerReloadListener {
    private static final Codec<List<Privilege<?, ?>>> CODEC = Codec.list(BSRegistries.PRIVILEGE_TYPE.byNameCodec().dispatch(Privilege::type, s -> s.serializer().CODEC));

    private final Map<PrivilegeType<?, ?>, PrivilegeMap<?, ?>> privileges = new HashMap<>();

    @Override
    @SuppressWarnings("unchecked")
    protected <K, V> PrivilegeMap<K, V> getMapImpl(PrivilegeType<K, V> type) {
        return (PrivilegeMap<K, V>) privileges.get(type);
    }

    @Override
    protected void addPrivilegesImpl(Collection<Privilege<?, ?>> privileges) {
        for (var privilege : privileges) {
            addPrivilege(privilege);
        }
    }

    private <K, V> void addPrivilege(Privilege<K, V> privilege) {
        var type = privilege.type();
        this.privileges.putIfAbsent(privilege.type(), new PrivilegeMap<>());
        getMap(type).addPrivilege(privilege);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <K, V> boolean canAccessImpl(Player player, PrivilegeType<K, V> type, K object) {
        var map = (PrivilegeMap<K, V>) privileges.get(type);
        return map == null || map.canAccess(player, object);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <K, V> Privilege<K, V> getPrivilegeImpl(PrivilegeType<K, V> type, K object) {
        return ((PrivilegeMap<K, V>) privileges.get(type)).getPrivilege(object);
    }

    @Override
    public Collection<PrivilegeType<?, ?>> getTypesImpl() {
        return privileges.keySet();
    }

    @Override
    public void clearImpl() {
        privileges.clear();
    }

    @Override
    public Map<PrivilegeType<?, ?>, PrivilegeMap<?, ?>> getPrivilegesImpl() {
        return this.privileges;
    }

    @Override
    public void onResourceManagerReload(ResourceManager manager) {
        clearImpl();
        for (var entry : manager.listResources("stages", path -> path.getPath().endsWith(".json")).entrySet()) {
            var id = entry.getKey();
            var resource = entry.getValue();

            try (var reader = new InputStreamReader(resource.open())) {
                var json = JsonParser.parseReader(reader);

                var privileges = CODEC.parse(JsonOps.INSTANCE, json).getOrThrow(JsonParseException::new);
                addPrivileges(privileges);
            } catch (IOException exception) {
                BSUtil.LOG.error("Could not load privileges from '{}'", id);
            } catch (JsonSyntaxException exception) {
                BSUtil.LOG.error("Could not parse '{}' privilege syntax: {}", id, exception.getMessage());
                throw exception;
            }
        }
        S2CPackets.INSTANCE.clearPrivileges();
        S2CPackets.INSTANCE.sendPrivileges();
    }
}
