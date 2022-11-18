package ru.practicum.shareit.comment;

import lombok.experimental.UtilityClass;

@UtilityClass
public class CommentMapper {

    public static CommentsDTO toCommentsDTO(Comments comments) {
        return new CommentsDTO(comments.getId(), comments.getText(), comments.getAuthor().getName(),
                comments.getCreated());
    }
}
