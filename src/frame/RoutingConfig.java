package frame;

import org.restlet.Application;
import org.restlet.Restlet;
import org.restlet.routing.Router;

import controller.FileResourceController;
import controller.NotificationController;
import controller.TransactionController;
import controller.TrashController;
import controller.UploadController;
import controller.UserController;
import controller.VersionController;

public class RoutingConfig extends Application {

    @Override
    public Restlet createInboundRoot() {
        // TODO Auto-generated method stub
    	
        Router router = new Router(getContext());
        router.attach("/user/{userId}/files/{type}", new UploadController());
        router.attach("/user/{userId}/file/{fileId}", new FileResourceController());
        router.attach("/user/{userId}/transaction", new TransactionController());
        router.attach("/user/{userId}/sync/{tid}", new TransactionController());
        router.attach("/user/{userId}/update", new NotificationController());
        router.attach("/user/{userId}/revisions", new VersionController());
        router.attach("/user/{userId}/trash", new TrashController());
        router.attach("/login", new UserController());
        return router;
    }
}
