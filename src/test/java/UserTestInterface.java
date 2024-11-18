/**
 * Interface defining test cases for User class.
 *
 * @author William Thain, Fox Christiansen, Jackson Shields, Peter Bui: lab sec 12
 * @version Nov 2, 2024
 */
public interface UserTestInterface {
    void testBasicUser();

    void testEmptyUser();

    void testNullUser();

    void testUserWithProfile();

    void testUserWithFriends();

    void testUserWithBlocks();

    void testUserWithBirthday();

    void testUserWithInvalidBirthday();

    void testUserWithProfilePic();

    void testUserToStringWithNulls();
} 