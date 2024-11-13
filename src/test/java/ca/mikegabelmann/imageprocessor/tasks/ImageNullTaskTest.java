package ca.mikegabelmann.imageprocessor.tasks;

import ca.mikegabelmann.imageprocessor.events.ImageProcessEvent;
import ca.mikegabelmann.imageprocessor.events.ImageProcessEventType;
import ca.mikegabelmann.imageprocessor.listeners.ImageMessageEventListener;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


class ImageNullTaskTest {

    @Test
    @DisplayName("test minimum sleep time")
    void test1_constructor() {
        Assertions.assertEquals(ImageNullTask.MIN_WAIT, new ImageNullTask(ImageNullTask.MIN_WAIT - 1).getSleeptime());
    }

    @Test
    @DisplayName("test maximum sleep time")
    void test2_constructor() {
        Assertions.assertEquals(ImageNullTask.MAX_WAIT, new ImageNullTask(ImageNullTask.MAX_WAIT + 1).getSleeptime());
    }

    @Test
    @DisplayName("test other sleep time")
    void test3_constructor() {
        Assertions.assertEquals(5000L, new ImageNullTask(5000L).getSleeptime());
    }

    @Test
    @DisplayName("busy wait")
    void test4_processTask() throws Exception {
        ImageMessageEventListener iml = event -> {
            //do nothing
        };

        ImageNullTask task = new ImageNullTask(1500L);
        long t = System.currentTimeMillis();

        synchronized(task) {
            //we have to synchronize on ourselves otherwise we get a "current thread is not owner"
            task.processTask(new ImageProcessEvent(ImageProcessEventType.PRIORITY_MEDIUM, iml, null));
        }

        Assertions.assertTrue(System.currentTimeMillis() - t >= 1500L);
    }

}