package ru.nir.repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.UUID;
import org.springframework.stereotype.Repository;
import ru.nir.model.User;

//репозиторий в памяти приложения

@Repository
public class UserRepository {

    Map<User, String> userStringMap = new HashMap<>();

    //найти пользователя по фамилии
    public Optional<User> findUserByUsername(String username) {
        User userFinal = null;
        for (Entry<User, String> pair : userStringMap.entrySet()) {
            User user = pair.getKey();
            if (user.getUsername().equals(username)) {
                userFinal = user;
                break;
            }
        }
        return Optional.ofNullable(userFinal);
    }

    //добавить пользователя
    public User saveUser(User user) {
        UUID uuid = UUID.randomUUID();
        userStringMap.put(user, uuid.toString());
        return user;
    }

    //найти пользака по имени
    public Boolean existsByUsername(String username) {
        Boolean result = false;
        for (Entry<User, String> pair : userStringMap.entrySet()) {
            User user = pair.getKey();
            if (user.getUsername().equals(username)) {
                result = true;
            }
        }
        return result;
    }

    public Boolean existsByEmail(String email) {
        Boolean result = false;
        for (Entry<User, String> pair : userStringMap.entrySet()) {
            User user = pair.getKey();
            if (user.getEmail().equals(email)) {
                result = true;
            }
        }
        return result;
    }

    public Optional<User> findUserById(String id) {
        Optional<Entry<User, String>> findElement = userStringMap.entrySet().stream()
            .filter(element -> element.getValue().equals(id))
            .findFirst();
        User findUser =  findElement.get().getKey();
        return Optional.ofNullable(findUser);
    }

    public List<User> findAll(){
        List<User> listUsers = new ArrayList<>();
        for(Entry<User,String> pair: userStringMap.entrySet()){
            listUsers.add(pair.getKey());
        }
        return listUsers;
    }

}
