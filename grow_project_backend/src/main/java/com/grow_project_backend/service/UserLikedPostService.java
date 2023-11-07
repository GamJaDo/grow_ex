package com.grow_project_backend.service;

import com.grow_project_backend.entity.PostEntity;
import com.grow_project_backend.entity.UserEntity;
import com.grow_project_backend.repository.PostRepository;
import com.grow_project_backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpSession;
import java.util.List;

@Service
public class UserLikedPostService {
  private final UserRepository userRepository;
  private final PostRepository postRepository;

  @Autowired
  public UserLikedPostService(UserRepository userRepository, PostRepository postRepository) {
    this.userRepository = userRepository;
    this.postRepository = postRepository;
  }

  public int addOrCancelLikeUser(Long postId, HttpSession session) {
    PostEntity post = postRepository.findById(postId)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "게시물을 찾을 수 없습니다."));

    UserEntity user = (UserEntity) session.getAttribute("user");
    if (user == null) {
      throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "로그인을 해야 댓글을 작성할 수 있습니다.");
    }

    if (post.getLikedUsers().contains(user)) post.getLikedUsers().remove(user);
    else post.getLikedUsers().add(user);
    postRepository.save(post);
    return post.getLikedUsers().size();
  }
  public int getLikeUserNumber(Long postId) {
    PostEntity post = postRepository.findById(postId)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "게시물을 찾을 수 없습니다."));
    return post.getLikedUsers().size();
  }
  public List<PostEntity> getLikePosts(Long userId) {
    UserEntity user = userRepository.findById(userId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "사용자를 찾을 수 없습니다."));
    return user.getLikedPosts();
  }
}
