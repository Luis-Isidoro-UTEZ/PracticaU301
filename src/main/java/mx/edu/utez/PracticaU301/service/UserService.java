package mx.edu.utez.PracticaU301.service;

import mx.edu.utez.PracticaU301.dto.CreateUserDTO;
import mx.edu.utez.PracticaU301.model.User;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final Map<Long, User> storage = new LinkedHashMap<>();
    private final AtomicLong idGen = new AtomicLong(1);

    // Constructor para poblar datos iniciales
    public UserService() {
        save(new CreateUserDTO("Ana", "Perez", 18, "ana@example.com"));
        save(new CreateUserDTO("Carlos", "Lopez", 60, "carlos@example.com"));
        save(new CreateUserDTO("Daniela", "Ruiz", 25, "daniela@example.com"));
    }

    public User save(CreateUserDTO dto) {
        Long id = idGen.getAndIncrement();
        User u = new User(id, dto.getName(), dto.getLastname(), dto.getAge(), dto.getEmail());
        storage.put(id, u);
        return u;
    }

    public Optional<User> findById(Long id) {
        return Optional.ofNullable(storage.get(id));
    }

    public List<User> findAll(int page, int size, String search) {
        List<User> all = new ArrayList<>(storage.values());
        if (search != null && !search.isBlank()) {
            String s = search.toLowerCase();
            all = all.stream()
                    .filter(u -> u.getName().toLowerCase().contains(s)
                            || u.getLastname().toLowerCase().contains(s)
                            || u.getEmail().toLowerCase().contains(s))
                    .collect(Collectors.toList());
        }
        int from = page * size;
        if (from >= all.size()) return Collections.emptyList();
        int to = Math.min(from + size, all.size());
        return all.subList(from, to);
    }
}