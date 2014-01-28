This is sample that is using worker.

In verticle, it accepts http request and send the event to backend worker by using Event Bus.
In the backend worker, it will handles long time transaction and reply to http handler verticle.
