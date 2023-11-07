// PostService.java
package com.grow_project_backend.service;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.grow_project_backend.dto.AllPostsDto;
import com.grow_project_backend.dto.CreatePostDto;
import com.grow_project_backend.dto.PostDto;
import com.grow_project_backend.dto.UpdatePostDto;
import com.grow_project_backend.entity.PostEntity;
import com.grow_project_backend.entity.UserEntity;
import com.grow_project_backend.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpSession;

@Service
public class PostService {
	
	@Autowired
    private PostRepository postRepository;
	
	@Autowired
    private AmazonS3Client amazonS3Client;
	
	// 이미지를 S3에 업로드하고 URL을 반환하는 메서드
    private String uploadImageToS3(MultipartFile image, String dir) throws IOException {
        if (image != null && !image.isEmpty()) {
            String key = dir + "/" + image.getOriginalFilename();
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentLength(image.getSize());
            metadata.setContentType(image.getContentType());
            amazonS3Client.putObject("gpb-bucket", key, image.getInputStream(), metadata);
            return amazonS3Client.getUrl("gpb-bucket", key).toString();
        }
        return null;
    }
    
    // 생성
    public PostDto createPost(CreatePostDto createPostDto, MultipartFile image, HttpSession session) throws IOException {
        UserEntity user = (UserEntity) session.getAttribute("user");
        if (user == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "로그인을 해야 게시물을 작성할 수 있습니다.");
        }
        
        PostEntity postEntity = new PostEntity();
        postEntity.setUser(user);
        postEntity.setTitle(createPostDto.getPostTitle());
        postEntity.setContents(createPostDto.getPostContents());
        postEntity.setCategory(createPostDto.getPostCategory());
        
        if (image != null && !image.isEmpty()) {
            String imageUrl = uploadImageToS3(image, "post-images"); // "post-images"는 S3 내의 폴더명
            postEntity.setPostImageUrl(imageUrl);
        }
        
        PostEntity savedPost = postRepository.save(postEntity);

        return new PostDto(
            savedPost.getTitle(),
            savedPost.getContents(),
            savedPost.getCategory(),
            savedPost.getLikedUsers().contains(user),
            savedPost.getPostImageUrl()
        );
    }
    
    // 읽기
    public PostDto getPostById(Long id, HttpSession session) {
        UserEntity user = (UserEntity) session.getAttribute("user");
        if (user == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "로그인을 해야 게시물을 작성할 수 있습니다.");
        }

        PostEntity postEntity = postRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "게시물이 존재하지 않습니다."));

        return new PostDto(
            postEntity.getTitle(),
            postEntity.getContents(),
            postEntity.getCategory(),
            postEntity.getLikedUsers().contains(user),
            postEntity.getPostImageUrl()
        );
    }
    
    // 모두 읽기
    public List<AllPostsDto> getAllPosts() {
        List<PostEntity> postEntities = postRepository.findAll();
        List<AllPostsDto> postDtos = postEntities.stream().map(postEntity -> new AllPostsDto(
            postEntity.getTitle(),
            postEntity.getContents(),
            postEntity.getCategory(),
        	postEntity.getPostImageUrl())
        ).collect(Collectors.toList());
        return postDtos;
    }
    
    // 수정
    public PostDto updatePost(Long id, UpdatePostDto updatePostDto, MultipartFile image, HttpSession session) throws IOException {
        UserEntity user = (UserEntity) session.getAttribute("user");
        if (user == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "로그인을 해야 게시물을 작성할 수 있습니다.");
        }
        
        PostEntity postEntity = postRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "게시물이 존재하지 않습니다."));

        postEntity.setTitle(updatePostDto.getPostTitle());
        postEntity.setContents(updatePostDto.getPostContents());
        postEntity.setCategory(updatePostDto.getPostCategory());
        
        if (image != null && !image.isEmpty()) {
            String imageUrl = uploadImageToS3(image, "post-images");
            postEntity.setPostImageUrl(imageUrl);
        }
        
        PostEntity updatedPost = postRepository.save(postEntity);

        return new PostDto(
            updatedPost.getTitle(),
            updatedPost.getContents(),
            updatedPost.getCategory(),
            updatedPost.getLikedUsers().contains(user),
            updatedPost.getPostImageUrl()
        );
    }
    
    // 삭제
    public void deletePost(Long id) {
        PostEntity postEntity = postRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "게시물이 존재하지 않습니다."));

        postRepository.delete(postEntity);
    }
}
