package ru.practicum.shareit.item;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.shareit.user.User;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long> {

    List<Item> findAllByOwner(User owner);

    @Query(value = "SELECT * FROM items as i where i.available = true and (LOWER(i.name) LIKE CONCAT('%',LOWER(:text),'%') or LOWER(i.description) LIKE CONCAT('%',LOWER(:text),'%'))",nativeQuery = true)
    List<Item> findAllByAvailableAndNameOrDescription(@Param("text") String text);

}