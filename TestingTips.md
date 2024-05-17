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

+ Service Class Testing Example:
```
@ExtendWith(MockitoExtension.class)
public class UserServicesTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServicesImpl userServices;

    @Test
    void testGetAllUsers() {
        // Arrange
        List<User> usersList = new ArrayList<>();
        Sort sort = Sort.by("fullName");
        Pageable pageable = PageRequest.of(0, 50, sort);
        Page<User> usersPage2 = Mockito.mock(Page.class);
        when(userRepository.findAll(pageable)).thenReturn(usersPage2);

        // Act
        List<UserDTO> actualList = userServices.getAllUsers(0);

        // Assert
        assertThat(actualList).isEqualTo(usersList);

    }
}
```

+ Controller Class Testing Example:
```
@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(controllers = HomePage.class)
@ExtendWith(MockitoExtension.class)
public class HomePageTest {

  @MockBean
  private JwtHelper jwtHelper;

  @MockBean
  private UserDetailsService userDetailsService;

  @Autowired
  private MockMvc mockMvc;

  @Test
  @WithMockUser(roles = "user")
  void testWelcome() throws Exception {
    mockMvc
      .perform(
        MockMvcRequestBuilders
          .get("/")
          .with(SecurityMockMvcRequestPostProcessors.jwt())
      )
      .andExpect(MockMvcResultMatchers.status().isOk());
  }
}
```