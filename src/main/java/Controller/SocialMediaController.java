package Controller;

import com.fasterxml.jackson.databind.ObjectMapper;

import Service.*;
import Model.*;
import io.javalin.Javalin;
import io.javalin.http.Context;

/**
 * TODO: You will need to write your own endpoints and handlers for your controller. The endpoints you will need can be
 * found in readme.md as well as the test cases. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
public class SocialMediaController {
    /**
     * In order for the test cases to work, you will need to write the endpoints in the startAPI() method, as the test
     * suite must receive a Javalin object from this method.
     * @return a Javalin app object which defines the behavior of the Javalin controller.
     */
    public Javalin startAPI() {
        Javalin app = Javalin.create();
        ObjectMapper om = new ObjectMapper();
        AccountService as = new AccountService();
        MessageService ms = new MessageService();

        app.get("example-endpoint", this::exampleHandler);
        
        app.post("/register",  ctx -> { //register account
            String json = ctx.body();
            Account acc = as.createAccount(om.readValue(json, Account.class));

            if (acc == null) { //account failed to persist to db
                ctx.status(400);
                //ctx.result("Account not created, password must be greater than 4 characters / username cannot be blank / account already exists");
            } else{ //account successfully was created in db
                ctx.status(200);
                ctx.json(acc);
            }
            
            
        });

        app.post("/login", ctx -> { //login to account
            String json = ctx.body();
            Account acc = as.verifyAccount(om.readValue(json, Account.class));

            if (acc != null) { //account exists in db
                ctx.status(200);
                ctx.json(acc);
            } else { //account failed to match an account on db
                ctx.status(401);
            }
        });

        app.post("/messages", ctx -> {
            String json = ctx.body();
            Message msg = ms.createMessage(om.readValue(json, Message.class));
            
            if (msg != null) {//message successfully created to db
                ctx.status(200);
                ctx.json(msg);
            } else {//message failed to persist into db
                ctx.status(400);
            }
        });

        app.patch("messages/{message_id}", ctx -> {
            String messageid = ctx.pathParam("message_id");
            String json = ctx.body();
            Message newMessage = om.readValue(json, Message.class);
            
            try {
                newMessage.setMessage_id(Integer.parseInt(messageid));
                Message msg = ms.updateMessage(newMessage);

                if (msg != null) {
                    ctx.status(200);
                    ctx.json(msg);
                } else{
                    ctx.status(400);
                }
                
            } catch (NumberFormatException e) {
                System.out.println(e.getMessage());
                ctx.status(400);
            }
            
        });

        app.get("/messages/{message_id}", ctx -> {
            String messageid = ctx.pathParam("message_id");
            
            try {
                Message msg = ms.findMessage(Integer.parseInt(messageid));

                if (msg != null) {
                    ctx.json(msg);
                }
                
            } catch (NumberFormatException e) {
                System.out.println(e.getMessage());
                ctx.result("");
            }

            ctx.status(200);

        });

        app.delete("messages/{message_id}", ctx -> {
            String messageid = ctx.pathParam("message_id");

            try {
                Message msg = ms.deleteMessage(Integer.parseInt(messageid));

                if (msg != null) {
                    ctx.json(msg);
                }
                
            } catch (NumberFormatException e) {
                System.err.println(e.getMessage());
            }

            ctx.status(200);
        });


        return app;
    }

    /**
     * This is an example handler for an example endpoint.
     * @param context The Javalin Context object manages information about both the HTTP request and response.
     */
    private void exampleHandler(Context context) {
        context.json("sample text");
    }


}