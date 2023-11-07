package com.grow_project_backend.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PostDto {
	private String postTitle;
  private String postContents;
  private String postCategory;
  private boolean isLiked;
  private String postImageUrl;
}
