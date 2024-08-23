package com.mygdx.scngame.event;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.backends.headless.HeadlessApplication;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

class GlobalEventBusTest {

    static GlobalEventBus globalEventBus;
    static Application app;

    @BeforeAll
    static void setUpBeforeClass() {
        app = new HeadlessApplication(new ApplicationAdapter() {});
        globalEventBus = GlobalEventBus.getInstance();
    }

    @AfterAll
    static void tearDownAfterClass() {
        app.exit();
    }

    @BeforeEach
    void setup() {
        globalEventBus.clearAllListeners();
    }

    static class TestDialogListener implements DialogEventListener {

        public String lastID = null;
        public int onDialogStartCount = 0;
        public int onDialogEndCount = 0;

        @Override
        public void onDialogStart(String id) {
            lastID = id;
            onDialogStartCount++;
        }

        @Override
        public void onDialogEnd() {
            onDialogEndCount++;
        }
    }

    @ParameterizedTest
    @ValueSource(strings = {"TestDialogID", "CRAZY VALUE", "", "  JFIUAHF OIUADFPOUADUFOIUWQOHJFA"})
    void addDialogListener(String testID) {
        TestDialogListener listener = new TestDialogListener();
        globalEventBus.addDialogListener(listener);

        assertEquals(0, listener.onDialogStartCount);
        assertEquals(0, listener.onDialogEndCount);
        assertNull(listener.lastID);

        globalEventBus.startDialog(testID);
        assertEquals(1, listener.onDialogStartCount);
        assertEquals(0, listener.onDialogEndCount);
        assertEquals(testID, listener.lastID);

        globalEventBus.endDialog();
        assertEquals(1, listener.onDialogStartCount);
        assertEquals(1, listener.onDialogEndCount);
    }

    @Test
    void removeDialogListener() {
        TestDialogListener listener = new TestDialogListener();

        globalEventBus.addDialogListener(listener);
        globalEventBus.removeDialogListener(listener);

        globalEventBus.startDialog("TestDialogID");
        globalEventBus.endDialog();

        assertEquals(0, listener.onDialogStartCount);
        assertEquals(0, listener.onDialogEndCount);
        assertNull(listener.lastID);
    }

    @Test
    void clearDialogListeners() {
    }

    @Test
    void addMapChangeListener() {
    }

    @Test
    void removeMapChangeListener() {
    }

    @Test
    void clearMapChangeListener() {
    }
}