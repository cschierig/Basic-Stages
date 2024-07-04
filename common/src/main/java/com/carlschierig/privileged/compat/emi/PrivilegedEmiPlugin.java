package com.carlschierig.privileged.compat.emi;

import com.carlschierig.privileged.PrivilegedClient;
import com.carlschierig.privileged.api.privilege.PrivilegeTypes;
import com.carlschierig.privileged.api.privilege.PrivilegesManager;
import com.carlschierig.privileged.impl.util.Util;
import dev.emi.emi.api.EmiEntrypoint;
import dev.emi.emi.api.EmiPlugin;
import dev.emi.emi.api.EmiRegistry;

@EmiEntrypoint
public class PrivilegedEmiPlugin implements EmiPlugin {

    @Override
    public void register(EmiRegistry registry) {
        var player = PrivilegedClient.getPlayer();
        if (player != null) {
            Util.LOG.info("Modifying EMI registry");
            registry.removeEmiStacks(stack ->
                    !PrivilegesManager.canAccess(player, PrivilegeTypes.ITEM, stack.getItemStack().getItem())
            );
            registry.removeRecipes(recipe ->
                    recipe.getOutputs().stream()
                            .anyMatch(output -> !PrivilegesManager.canAccess(player, PrivilegeTypes.ITEM, output.getItemStack().getItem()))
            );
        }
    }
}
