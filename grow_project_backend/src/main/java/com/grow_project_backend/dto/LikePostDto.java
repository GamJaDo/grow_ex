package com.grow_project_backend.dto;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LikePostDto {
  private Long postId;
  private String title;
  private String postImage;
}
