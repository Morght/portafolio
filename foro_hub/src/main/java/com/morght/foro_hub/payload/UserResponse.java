package com.morght.foro_hub.payload;

import com.morght.foro_hub.models.User;

public record UserResponse(
                Long id,
                String name,
                String email) {
        public UserResponse(User user) {
                this(user.getId(), user.getName(), user.getEmail());
        }
}
