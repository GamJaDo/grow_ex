package com.grow_project_backend.entity;

import javax.persistence.*;

import lombok.Getter;
import lombok.Setter;
import org.springframework.lang.NonNull;

import java.util.*;

@Setter
@Getter
@Table(name = "User")
@Entity
public class UserEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NonNull
	@Column(unique = true)
	private String loginId;

	@NonNull
	private String password;

	@NonNull
	private String name;

	@OneToMany(mappedBy = "user")
	private List<CommentEntity> comments = new ArrayList<>();

	@OneToMany(mappedBy = "user")
	private List<PostEntity> posts = new ArrayList<>();

	@ManyToMany(mappedBy = "likedUsers")
	private List<PostEntity> likedPosts = new ArrayList<>();

	@Override
	public int hashCode() {
		return this.loginId.hashCode();
	}
	@Override
	public boolean equals(Object o) {
		if (o == this) return true;
		if (o == null) return false;
		if (o.getClass() != this.getClass()) return false;
		return Objects.equals(this.id, ((UserEntity) o).id);
	}
}
