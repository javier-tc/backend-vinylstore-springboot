package com.vinylstore.config;

import com.vinylstore.auth.model.User;
import com.vinylstore.auth.repository.UserRepository;
import com.vinylstore.product.model.Product;
import com.vinylstore.product.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
public class DataInitializer implements CommandLineRunner {
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private ProductRepository productRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Override
    public void run(String... args) throws Exception {
        initializeAdminUser();
        initializeProducts();
    }
    
    private void initializeAdminUser() {
        if (!userRepository.existsByEmail("admin@vinylstore.com")) {
            User admin = new User();
            admin.setEmail("admin@vinylstore.com");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setFirstName("Administrador");
            admin.setLastName("VinylStore");
            admin.setRole(User.Role.ADMIN);
            userRepository.save(admin);
            System.out.println("Usuario administrador creado: admin@vinylstore.com / admin123");
        }
    }
    
    private void initializeProducts() {
        if (productRepository.count() == 0) {
            List<Product> initialProducts = List.of(
                createProduct("Abbey Road", "The Beatles", "Rock", 
                    "Álbum icónico de 1969", new BigDecimal("45000"), 10,
                    "https://th.bing.com/th/id/R.9b5bfaa303595bec7d186032fdf46282?rik=P%2bmzEURyBGZvDQ&pid=ImgRaw&r=0"),
                createProduct("The Dark Side of the Moon", "Pink Floyd", "Progressive Rock",
                    "Grabación revolucionaria de rock progresivo", new BigDecimal("52000"), 8,
                    "https://tse4.mm.bing.net/th/id/OIP.pNbqWq1EjFsAtHnJwKoYcwHaHa?rs=1&pid=ImgDetMain&o=7&rm=3"),
                createProduct("Kind of Blue", "Miles Davis", "Jazz",
                    "Obra maestra del jazz modal", new BigDecimal("48000"), 5,
                    "https://tse3.mm.bing.net/th/id/OIP.bq5eSwrarMlhhH3fRZmQHwHaGs?rs=1&pid=ImgDetMain&o=7&rm=3"),
                createProduct("Led Zeppelin IV", "Led Zeppelin", "Rock",
                    "Álbum emblemático del rock", new BigDecimal("47000"), 12,
                    "https://http2.mlstatic.com/D_NQ_NP_908718-MLB51428359896_092022-O.webp"),
                createProduct("Miles Davis Quintet", "Miles Davis", "Jazz",
                    "Jazz legendario", new BigDecimal("55000"), 7,
                    "https://th.bing.com/th/id/R.e062b28d9a1590221ecc67ba727aad28?rik=e316YypZCfcCqg&riu=http%3a%2f%2fwww.progarchives.com%2fprogressive_rock_discography_covers%2f3906%2fcover_51772062016_r.jpg&ehk=M594PasbB9Xo2OtLA%2f%2fh9SgN7ffAehxTtAQWF%2bjbZj4%3d&risl=&pid=ImgRaw&r=0")
            );
            productRepository.saveAll(initialProducts);
            System.out.println("Productos iniciales cargados: " + initialProducts.size());
        }
    }
    
    private Product createProduct(String title, String artist, String genre, 
                                   String description, BigDecimal price, Integer stock, String imageUrl) {
        Product product = new Product();
        product.setTitle(title);
        product.setArtist(artist);
        product.setGenre(genre);
        product.setDescription(description);
        product.setPrice(price);
        product.setStock(stock);
        product.setImageUrl(imageUrl);
        return product;
    }
}

