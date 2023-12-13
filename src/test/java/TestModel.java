import org.suai.todo.model.*;

import org.junit.jupiter.api.Test;

import java.io.File; 

public class TestModel {
    
    @Test
    public void test1() throws Exception {
        try {
            File f= new File("/tmp/todo1.db"); 
            f.delete();
        } catch (Exception e){}
        
        Model.init("/tmp/todo1.db");  
    }
    
    @Test
    public void test2() throws Exception {
        User user = new User("user1", "pass1");
        Model.save(user);
    }
    
    @Test
    public void test3() throws Exception {
        User user = Model.getUserByName("user1");        
        assert user.name.equals("user1");
        Integer id = user.id;
        assert id == 1;       
        assert user.isValidPassword("pass1");
    }
    
}
