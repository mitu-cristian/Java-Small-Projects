package com.dev.cristian.myfirstwebapp.todo;

import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;

import java.time.LocalDate;
import java.util.List;

@Controller
@SessionAttributes("name")
public class TodoControllerJpa {

    private TodoRepository todoRepository;

//    Autowiring
    public TodoControllerJpa(TodoRepository todoRepository) {
        super();
        this.todoRepository = todoRepository;
    }

    // /list-todos
    @RequestMapping("list-todos")
    public String listAllTodos(ModelMap model) {
        String username = getLoggedinUsername(model);
        List<Todo> todos =  todoRepository.findByUsername(username);
        model.addAttribute("todos", todos);
        return "listTodos";
    }

    // add-todo - GET
    @RequestMapping(value = "add-todo", method = RequestMethod.GET)
    public String showNewTodoPage(ModelMap model) {
        Todo todo = new Todo(0, getLoggedinUsername(model), "", LocalDate.now(), false);
        model.put("todo", todo);
        return "todo";
    }

    //add-todo - POST
    @RequestMapping(value = "add-todo", method = RequestMethod.POST)
    public String addNewTodo(ModelMap model, @Valid Todo todo, BindingResult result) {
        if(result.hasErrors()) {
            return "todo";
        }

        String username = getLoggedinUsername(model);
        todo.setUsername(username);
        todoRepository.save(todo);

//        todoService.addTodo(getLoggedinUsername(model), todo.getDescription(), todo.getTargetDate(), todo.getDone());
        return "redirect:list-todos";
    }

    //delete a todo
    @RequestMapping("delete-todo")
    public String deleteTodo(@RequestParam Integer id) {
        todoRepository.deleteById(id);
        return "redirect:list-todos";
    }

    //update a todo - redirect to the addtodo page
    @RequestMapping(value="update-todo", method=RequestMethod.GET)
    public String showUpdateTodoPage(@RequestParam Integer id, ModelMap model) {
        Todo todo = todoRepository.findById(id).get();
        model.addAttribute("todo", todo);
        return "todo";
    }

    //update a todo - updating the todo itself
    @RequestMapping(value="update-todo", method=RequestMethod.POST)
    public String updateTodo(ModelMap model, @Valid Todo todo, BindingResult result) {
        if(result.hasErrors())
            return "todo";

        String username = getLoggedinUsername(model);
        todo.setUsername(username);

        todoRepository.save(todo);
        return "redirect:list-todos";
    }

    private static String getLoggedinUsername(ModelMap model) {
        Authentication authentication = SecurityContextHolder
                .getContext()
                .getAuthentication();
        return authentication.getName();
    }
}
