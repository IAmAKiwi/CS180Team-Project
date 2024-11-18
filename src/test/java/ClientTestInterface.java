import java.io.IOException;


/**
 * A frammework to run Client tests
 *
 * @author William Thain, Fox Christiansen, Jackson Shields, Bui Dinh Tuan Anh:
 *         lab sec 12
 * @version Nov 17, 2024
 */
public interface ClientTestInterface {
    void testClientConstructor();

    void testGetUserList() throws IOException;

    void testSendMessage() throws IOException;

    void testDeleteMessage() throws IOException;

    void testAccessProfile() throws IOException;

    void testSaveProfile() throws IOException;

    void testRemoveFriend() throws IOException;

    void testAddFriend() throws IOException;

    void testUnblockUser() throws IOException;

    void testBlockUser() throws IOException;

    void testDeleteChat() throws IOException;

    void testSendImage() throws IOException;

    void testGetChat() throws IOException;

    void testGetFriendList() throws IOException;

    void testGetBlockList() throws IOException;

    void testIsFriendsOnly() throws IOException;

    void testSetFriendsOnly() throws IOException;

    void testSetProfilePic() throws IOException;

    void testGetProfilePic() throws IOException;

    void testLogin() throws IOException;

    void testRegister() throws IOException;

    void testLogout() throws IOException;

    void testDisconnect() throws IOException;
}