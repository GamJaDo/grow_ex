package com.grow_project_backend.entity;

import javax.persistence.*;

import lombok.Getter;
import lombok.Setter;
import org.springframework.lang.NonNull;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Setter
@Getter
@Table(name = "Post")
@Entity
public class PostEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String category;

	@NonNull
	private String title;

	private String contents;

	@NonNull
	private String postImageUrl;

	@OneToMany(mappedBy = "post")
	private List<CommentEntity> comments = new ArrayList<>();

	@ManyToOne
	private UserEntity user;

	@ManyToMany
	@JoinTable(
					name = "user_liked_post",
					joinColumns = @JoinColumn(name = "liked_post_id"),
					inverseJoinColumns = @JoinColumn(name = "liked_user_id")
	)
	private Set<UserEntity> likedUsers = new HashSet<>();
}
