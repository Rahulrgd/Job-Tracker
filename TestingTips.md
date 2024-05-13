# # Use Arrange, Act, & Assert techinque to test your methods:
+ Repository Testing Example:
```
@DataJpaTest
public class UserRepositoryTest {
    @Autowired
    private UserRepository userRepository;

    @Test
    void testFindByEmail() {
        
        // Arrange
        User user = new User();
        user.setEmail("rahi@gmail.com");
        user.setFullName("Rahi Turkar");
        user.setPassword("12345678");
        userRepository.save(user);

        // Act
        User actualUser = userRepository.findByEmail("rahi@gmail.com").get();

        // Assert
        assertThat(actualUser).isEqualTo(user);
    }
}
```