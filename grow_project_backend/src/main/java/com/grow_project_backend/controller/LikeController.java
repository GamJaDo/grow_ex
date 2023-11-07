package com.grow_project_backend.controller;

import com.grow_project_backend.dto.LikePostDto;
import com.grow_project_backend.dto.LikePostListDto;
import com.grow_project_backend.dto.LikeUserNumberDto;
import com.grow_project_backend.entity.PostEntity;
import com.grow_project_backend.repository.UserRepository;
import com.grow_project_backend.service.UserLikedPostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@RestController
@RequestMapping("/api/like")
public class LikeController {
  private final UserLikedPostService userLikedPostService;
  private final UserRepository userRepository;

  @Autowired
  public LikeController(UserLikedPostService userLikedPostService, UserRepository userRepository) {
    this.userLikedPostService = userLikedPostService;
    this.userRepository = userRepository;
  }

  @GetMapping("/update/{postId}")
  public ResponseEntity<LikeUserNumberDto> addOrCancelLike(@PathVariable Long postId, HttpSession session) {
    int likeUserNumber = userLikedPostService.addOrCancelLikeUser(postId, session);
    LikeUserNumberDto result = new LikeUserNumberDto();
    result.setPostId(postId);
    result.setLikeUserNumber(likeUserNumber);
    return new ResponseEntity<>(result, HttpStatus.OK);
  }
  @GetMapping("/post/{postId}")
  public ResponseEntity<LikeUserNumberDto> getPostLikedUserNumber(@PathVariable Long postId) {
    LikeUserNumberDto result = new LikeUserNumberDto();
    result.setLikeUserNumber(userLikedPostService.getLikeUserNumber(postId));
    result.setPostId(postId);
    return new ResponseEntity<>(result, HttpStatus.OK);
  }
  @GetMapping("/user/{userId}")
  public ResponseEntity<LikePostListDto> getUserLikedPosts(@PathVariable Long userId) {
    LikePostListDto result = new LikePostListDto();
    List<PostEntity> posts = userLikedPostService.getLikePosts(userId);
    Iterator<PostEntity> it = posts.iterator();
    List<LikePostDto> lp = new ArrayList<>(posts.size());
    while(it.hasNext()) {
      PostEntity pe = it.next();
      lp.add(LikePostDto.builder().postImage(pe.getPostImageUrl()).title(pe.getTitle()).postId(pe.getId()).build());
    }
    result.setData(lp);
    result.setUserId(userId);
    return new ResponseEntity<>(result, HttpStatus.OK);
  }
}