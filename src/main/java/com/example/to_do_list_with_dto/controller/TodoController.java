package com.example.to_do_list_with_dto.controller;

import com.example.to_do_list_with_dto.dto.TodoDto;
import com.example.to_do_list_with_dto.model.Todo;
import com.example.to_do_list_with_dto.model.User;
import com.example.to_do_list_with_dto.repository.TodoRepository;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/todos")
@RequiredArgsConstructor
public class TodoController {
    private final TodoRepository todoRepository;

    private User getCurrentUser(HttpSession session) {
        return (User) session.getAttribute("user");
    }

    @GetMapping
    public String list(HttpSession httpSession, Model model) {
        User user = getCurrentUser(httpSession);

        if(user == null) {
            return "redirect:/login";
        }

        List<Todo> list = todoRepository.findAllByUserId(user.getId());
        model.addAttribute("todos", list);

        return "todo-list";
    }

    @GetMapping("/add")
    public String addForm(HttpSession httpSession, Model model) {
        if (getCurrentUser(httpSession) == null) return "redirect:/login";

        model.addAttribute("todoDto", new TodoDto());

        return "todo-form";
    }

    @PostMapping("/add")
    public String add (
            @Valid @ModelAttribute TodoDto todoDto,
            BindingResult bindingResult,
            HttpSession httpSession
    ) {
        if (bindingResult.hasErrors()) return "todo-form";

        User user = getCurrentUser(httpSession);

        Todo todo = Todo.builder()
                .userId(user.getId())
                .title(todoDto.getTitle())
                .completed(todoDto.isCompleted())
                .build();

        todoRepository.save(todo);

        return "redirect:/todos";
    }

    @GetMapping("/edit/{id}")
    public String editForm(
            @PathVariable int id,
            Model model,
            HttpSession httpSession
    ) {
        User user = getCurrentUser(httpSession);

        if (user == null) return "redirect:/login";

        Todo todo = todoRepository.findByIdAndUserId(id, user.getId());
        TodoDto dto = new TodoDto();
        dto.setId(todo.getId());
        dto.setTitle(todo.getTitle());
        dto.setCompleted(todo.isCompleted());

        model.addAttribute("todoDto", dto);

        return "todo-form";
    }

    @PostMapping("/edit")
    public String edit(
            @Valid @ModelAttribute TodoDto todoDto,
            BindingResult bindingResult,
            HttpSession httpSession
    ) {
        if(bindingResult.hasErrors()) return "todo-form";

        User user = getCurrentUser(httpSession);
        Todo todo = Todo.builder()
                .id(todoDto.getId())
                .title(todoDto.getTitle())
                .completed(todoDto.isCompleted())
                .userId(user.getId()).build();

        todoRepository.update(todo);

        return "redirect:/todos";
    }

    @PostMapping("/delete/{id}")
    public String delete(
            @PathVariable int id,
            HttpSession httpSession
    ) {
        User user = getCurrentUser(httpSession);
        todoRepository.deleteByIdAndUserId(id, user.getId());

        return "redirect:/todos";
    }
}