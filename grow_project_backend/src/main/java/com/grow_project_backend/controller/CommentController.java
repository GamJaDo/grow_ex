package com.grow_project_backend.controller;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.grow_project_backend.dto.CommentDto;
import com.grow_project_backend.entity.CommentEntity;
import com.grow_project_backend.service.CommentService;

@RestController
@RequestMapping("/api/post/{postId}/comment")
public class CommentController {

    @Autowired
    private CommentService commentService;

    @PostMapping
    public ResponseEntity<CommentEntity> addComment(@PathVariable Long postId, 
    												@RequestBody CommentDto commentDto,
                                                    HttpSession session) {
    	
    	CommentEntity newComment = commentService.addComment(postId, commentDto.getCommentContents(), session);
        return new ResponseEntity<>(newComment, HttpStatus.CREATED);
    }
    
    @GetMapping
    public ResponseEntity<List<CommentDto>> getCommentsByPostId(@PathVariable Long postId) {
        List<CommentDto> comments = commentService.getCommentsByPostId(postId);
        return new ResponseEntity<>(comments, HttpStatus.OK);
    }
}

