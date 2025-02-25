package com.example.marketplace_crm.controller;

import com.example.marketplace_crm.Model.Tag;
import com.example.marketplace_crm.Service.Impl.TagService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@io.swagger.v3.oas.annotations.tags.Tag(name = "Tag Controller", description = "Управление тегами продуктов (получение, добавление и другие операции)")
@RestController
@RequestMapping("/tag")
public class TagController {

    private final TagService tagService;

    @Autowired
    public TagController(TagService tagService) {
        this.tagService = tagService;
    }

    @Operation(
            summary = "Получить список всех тегов",
            description = "Возвращает список всех доступных тегов. В случае отсутствия тегов, возвращает ошибку 404.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Список тегов успешно получен", content = @io.swagger.v3.oas.annotations.media.Content(mediaType = "application/json", schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = Tag.class))),
                    @ApiResponse(responseCode = "404", description = "Теги не найдены")
            }
    )
    @GetMapping("/list")
    public ResponseEntity<List<Tag>> list() {
        List<Tag> tags = tagService.getAll();
        if (!tags.isEmpty()) {
            return new ResponseEntity<>(tags, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
