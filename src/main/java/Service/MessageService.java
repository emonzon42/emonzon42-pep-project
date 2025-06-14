package Service;
import java.util.List;

import DAO.MessageDAO;
import Model.Message;

public class MessageService {
    
    private MessageDAO dao;

    public MessageService(){
        this.dao = new MessageDAO();
    }

    public MessageService(MessageDAO dao){
        this.dao = dao;
    }

    public List<Message> getAllMessages(){
        return dao.getAllMessages();
    }

    /*
     * creates message in db as long as conditions are met:
     * message isnt blank
     * message is less than 255 characters
     * posted_by refers to real user in db
     */
    public Message createMessage(Message msg){

        if (!msg.getMessage_text().isBlank() 
        && msg.getMessage_text().length() < 255 
        && new AccountService().verifyAccount(msg.posted_by) != null) {
            return dao.insertMessage(msg);
        } else {
            return null;
        }
    }
}
