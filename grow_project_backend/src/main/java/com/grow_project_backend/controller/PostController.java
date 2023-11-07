// PostController.java
package com.grow_project_backend.controller;

import com.grow_project_backend.dto.*;
import com.grow_project_backend.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("/api/post")
public class PostController {
	
	@Autowired
    private PostService postService;
	
    // 게시글 작성
    @PostMapping
    public ResponseEntity<PostDto> createPost(@RequestPart("createRequest") CreatePostDto createRequest,
            									@RequestPart(value = "image", required = false) MultipartFile image, // 이미지를 받는 부분
            									HttpSession session) throws IOException {
    	PostDto newPost = postService.createPost(createRequest, image, session);
    	
        return new ResponseEntity<>(newPost, HttpStatus.CREATED);
    }
    
    // 특정 번호의 게시글을 읽음
    @GetMapping("/{id}")
    public ResponseEntity<PostDto> getPostById(@PathVariable Long id, HttpSession session) {
    	PostDto post = postService.getPostById(id, session);
        return new ResponseEntity<>(post, HttpStatus.OK);
    }

    // 모든 게시글을 읽음
    @GetMapping
    public ResponseEntity<List<AllPostsDto>> getAllPosts() {
        List<AllPostsDto> posts = postService.getAllPosts();
        return new ResponseEntity<>(posts, HttpStatus.OK);
    }
    
    // 특정 번호의 게시글을 수정
    @PutMapping("/{id}")
    public ResponseEntity<PostDto> updatePost(
            @PathVariable Long id,
            @RequestPart("updateRequest") UpdatePostDto updatePostDto,
            @RequestPart(value = "image", required = false) MultipartFile image, // 이미지를 받는 부분
            HttpSession session) throws IOException {
        PostDto updatedPost = postService.updatePost(id, updatePostDto, image, session);
        return new ResponseEntity<>(updatedPost, HttpStatus.OK);
    }
    
    // 특정 번호의 게시글을 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePost(@PathVariable Long id) {
        postService.deletePost(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
