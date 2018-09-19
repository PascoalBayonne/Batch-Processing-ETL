Intercepting Step Execution

In this example i will be intercepting a step. using Listeners.
1- Move a file before reading it to another folder
2- Process it, if occur an error while reading then write this line which caused error into a file.
3- After all line have been processed then move the file again into another folder and append the file.name.done


ADVICES:
1-AWARE:
Moving a file really isn't a good idea in an AfterStep or even @beforeStep method. Generally, I try to avoid doing anything complex in listener methods.
You should have a separate step that takes care of it. You must use the "Tasklet" interface. (read the doc)
The biggest advantage there is that if the move fails, then you can restart directly on that step and just try the move again.

Correspondence table with listener interface
Listener interface	Annotation
JobExecutionListener

@beforeJob
@afterJob

StepExecutionListener

@BeforeStep
@AfterStep

ChunkListener

@BeforeChunk
@AfterChunk
@afterChunkError

ItemReadListener

@BeforeRead
@AfterRead
@OnReadError

ItemProcessListener

@beforeProcess
@afterProcess
@onProcessError

ItemWriteListener

@BeforeWrite
@AfterWrite
@OnWriteError