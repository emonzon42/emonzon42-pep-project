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
        AccountService service = new AccountService();

        app.get("example-endpoint", this::exampleHandler);
        
        app.post("/register",  ctx -> { //register account
            String json = ctx.body();
            Account acc = om.readValue(json, Account.class);
            
            Account createdAcc = service.createAccount(acc);

            if (createdAcc == null) { //account failed to persist to db
                ctx.status(400);
                //ctx.result("Account not created, password must be greater than 4 characters / username cannot be blank / account already exists");
            } else{ //account successfully was created in db
                ctx.status(200);
                ctx.json(createdAcc);
            }
            
            
        });

        app.post("/login", ctx -> { //login to account
            String json = ctx.body();
            Account acc = om.readValue(json, Account.class);
            Account verifiedAcc = service.verifyAccount(acc);
            if (verifiedAcc != null) { //account exists in db
                ctx.status(200);
                ctx.json(verifiedAcc);
            } else { //account failed to match an account on db
                ctx.status(401);
            }
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