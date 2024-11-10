# Image Processor
Is a threaded library for processing images.

## Project Information

## Usage

    //create image processor and start it (threaded)
    ImageProcessor ip = new ImageProcessor();
    ip.start();

    //create task, add it do event and add to queue
    ImageNullTask task = new ImageNullTask();
    ImageProcessEvent event = new ImageProcessEvent(ImageProcessEventType.PRIORITY_MEDIUM, listener, image, task);
    ip.getQueue().eventPerformed(event);

    'listener' must implement ImageMessageEventListener
    'image' must be a buffered image

    Alternatively you can create a task and then process it directly

## Local Usage
You will need to have the _maven_pom_ project installed into your local Maven 
repository or access to the GitHub repository version.

Maybe GitHub will allow anonymous access to the project repository in the future.
