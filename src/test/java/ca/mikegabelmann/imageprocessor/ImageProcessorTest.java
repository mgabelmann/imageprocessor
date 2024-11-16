package ca.mikegabelmann.imageprocessor;

import ca.mikegabelmann.imageprocessor.events.ImageMessageEvent;
import ca.mikegabelmann.imageprocessor.events.ImageMessageEventType;
import ca.mikegabelmann.imageprocessor.events.ImageProcessEvent;
import ca.mikegabelmann.imageprocessor.events.ImageProcessEventType;
import ca.mikegabelmann.imageprocessor.exception.ImageProcessorException;
import ca.mikegabelmann.imageprocessor.exception.ImageTaskException;
import ca.mikegabelmann.imageprocessor.listeners.ImageMessageEventListener;
import ca.mikegabelmann.imageprocessor.tasks.ErrorTask;
import ca.mikegabelmann.imageprocessor.tasks.ExitTask;
import ca.mikegabelmann.imageprocessor.tasks.ImageNullTask;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


class ImageProcessorTest implements ImageMessageEventListener {
    private static final Logger LOGGER = LoggerFactory.getLogger(ImageProcessorTest.class);

    public static final long SLEEP_TIME = 50L;

    /** we use this ImageProcessor for all tests */
    private ImageProcessor ip;


    @BeforeEach
    protected void setUp() throws Exception {
        this.ip = new ImageProcessor();
    }

    @AfterEach
    protected void tearDown() throws Exception {
        ;
    }

    @Test
    void test1_isRunning() {
        Assertions.assertFalse(ip.isRunning());
    }

    @Test
    public void test1_hasEventListener() {
        Assertions.assertFalse(ip.hasEventListener(this));
    }

    @Test
    public void test2_hasEventListener() {
        ip.addEventListener(this);
        Assertions.assertTrue(ip.hasEventListener(this));
    }

    @Test
    public void test3_hasEventListener() {
        Assertions.assertFalse(ip.hasEventListener(null));
    }

    @Test
    public void test1_addEventListener() {
        Assertions.assertFalse(ip.addEventListener(null));
        Assertions.assertTrue(ip.addEventListener(this));
        Assertions.assertFalse(ip.addEventListener(this));
    }

    @Test
    public void test1_removeEventListener() {
        Assertions.assertFalse(ip.removeEventListener(null));
    }

    @Test
    public void test2_removeEventListener() {
        Assertions.assertFalse(ip.removeEventListener(this));
        ip.addEventListener(this);
        Assertions.assertTrue(ip.removeEventListener(this));
    }

    @Test
    public void test1_getQueue() {
        Assertions.assertNotNull(ip.getQueue());
    }

    @Test
    @DisplayName("process an event and ensure response received")
    void test1_processEvent() {
        ImageMessageEventCounter counter = Mockito.spy(ImageMessageEventCounter.class);
        ImageNullTask nullTask = new ImageNullTask(500L);
        ImageProcessEvent ipe = new ImageProcessEvent(ImageProcessEventType.PRIORITY_MEDIUM, counter, null, nullTask);

        ip.processEvent(ipe);

        Mockito.verify(counter, Mockito.times(1)).eventPerformed(ArgumentMatchers.any(ImageMessageEvent.class));
    }

    @Test
    void test2_processEvent() {
        ImageMessageExceptionCounter counter = Mockito.spy(ImageMessageExceptionCounter.class);

        ErrorTask e1 = new ErrorTask(new ImageTaskException("task error"));
        ImageProcessEvent ipe = new ImageProcessEvent(ImageProcessEventType.PRIORITY_MEDIUM, counter, null, e1);
        ip.processEvent(ipe);

        Assertions.assertEquals(1, counter.getCount());
    }

    @Test
    void test3_processEvent() {
        ImageMessageExceptionCounter counter = Mockito.spy(ImageMessageExceptionCounter.class);

        ErrorTask e1 = new ErrorTask(new ImageProcessorException("processor error"));
        ImageProcessEvent ipe = new ImageProcessEvent(ImageProcessEventType.PRIORITY_MEDIUM, counter, null, e1);
        ip.processEvent(ipe);

        Assertions.assertEquals(1, counter.getCount());
    }

    @Test
    void test4_processEvent() {
        ImageMessageEventCounter counter = Mockito.spy(ImageMessageEventCounter.class);
        ImageMessageEventCounter counter2 = Mockito.spy(ImageMessageEventCounter.class);

        ImageNullTask nullTask = new ImageNullTask(250);
        ImageProcessEvent ipe = new ImageProcessEvent(ImageProcessEventType.PRIORITY_MEDIUM, counter, null, nullTask);

        //add a 2nd listener
        ip.addEventListener(counter2);

        ip.processEvent(ipe);

        Assertions.assertEquals(1, counter.getCount());
        Assertions.assertEquals(1, counter2.getCount());
    }

    @Test
    void test5_processEvent() {
        ImageMessageEventCounter counter = Mockito.spy(ImageMessageEventCounter.class);

        ExitTask task = new ExitTask();
        ImageProcessEvent ipe = new ImageProcessEvent(ImageProcessEventType.PRIORITY_MEDIUM, counter, null, task);

        ip.getQueue().eventPerformed(ipe);
        ip.run();

        //added in case our test fails for some reason
        ip.stopRunning();

        Assertions.assertEquals(1, counter.getCount());
    }

    @Test
    void test6_processEvent() throws InterruptedException {
        ImageMessageEventCounter counter = Mockito.spy(ImageMessageEventCounter.class);

        ImageProcessEvent ipe1 = new ImageProcessEvent(ImageProcessEventType.PRIORITY_MEDIUM, counter, null, new ImageNullTask(250));
        ImageProcessEvent ipe2 = new ImageProcessEvent(ImageProcessEventType.PRIORITY_MEDIUM, counter, null, new ImageNullTask(250));

        Thread t = new Thread(ip);
        t.start();

        ip.getQueue().eventPerformed(ipe1);
        ip.getQueue().eventPerformed(ipe2);

        //we need to sleep and hopefully thread completes in time
        Thread.sleep(550L);

        ip.stopRunning();
        t.interrupt();

        Assertions.assertEquals(2, counter.getCount());
    }

    @Test
    void test1_getRunningInstance() {
        Thread t = ImageProcessor.getRunningInstance(ip);
        Assertions.assertNotNull(t);
        Assertions.assertTrue(t.isAlive());

        ip.stopRunning();
        t.interrupt();
    }

    @Override
    public void eventPerformed(final ImageMessageEvent ime) {
        LOGGER.debug("received event {}", ime);
    }


    static class ImageMessageEventCounter implements ImageMessageEventListener {
        private int count = 0;

        @Override
        public synchronized void eventPerformed(ImageMessageEvent event) {
            LOGGER.debug("received event {}", event);
            count += 1;
        }

        public int getCount() {
            return count;
        }
    }

    static class ImageMessageExceptionCounter implements ImageMessageEventListener {
        private int count = 0;

        @Override
        public synchronized void eventPerformed(ImageMessageEvent event) {
            if (ImageMessageEventType.ERROR.equals(event.getStatus())) {
                LOGGER.debug("received event {}", event);
                count += 1;
            }
        }

        public int getCount() {
            return count;
        }
    }

}
