package com.carlschierig.privileged.test;

import net.minecraft.SharedConstants;
import net.minecraft.server.Bootstrap;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * https://stackoverflow.com/questions/43282798/in-junit-5-how-to-run-code-before-all-tests
 */
public class BeforeAllTestsExtension implements BeforeAllCallback {

    private static final Lock LOCK = new ReentrantLock();
    private static volatile boolean started = false;

    @Override
    public void beforeAll(ExtensionContext extensionContext) throws Exception {
        LOCK.lock();
        try {
            if (!started) {
                started = true;

                setup();

                extensionContext.getRoot().getStore(ExtensionContext.Namespace.GLOBAL).put("immersivecrafting", this);
            }
        } finally {
            LOCK.unlock();
        }
    }

    public void setup() {
        SharedConstants.tryDetectVersion();
        Bootstrap.bootStrap();
        // ImmersiveCraftingCommon.init();
    }
}
