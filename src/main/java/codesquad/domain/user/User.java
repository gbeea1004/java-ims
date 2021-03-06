package codesquad.domain.user;

import codesquad.UnAuthorizedException;
import codesquad.dto.UserDto;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import support.domain.AbstractEntity;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.validation.constraints.Size;

@Entity
public class User extends AbstractEntity {
    public static final GuestUser GUEST_USER = new GuestUser();

    @Size(min = 3, max = 20)
    @Column(unique = true, nullable = false, length = 20)
    private String userId;

    @Size(min = 6, max = 20)
    @Column(nullable = false, length = 20)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    @Size(min = 3, max = 20)
    @Column(nullable = false, length = 20)
    private String name;

    @Embedded
    private ProfileImage profileImage;

    public User() {
    }

    public User(String userId, String password, String name, ProfileImage profileImage) {
        this(0L, userId, password, name, profileImage);
    }

    public User(long id, String userId, String password, String name, ProfileImage profileImage) {
        super(id);
        this.userId = userId;
        this.password = password;
        this.name = name;
        this.profileImage = profileImage;
    }

    public String getUserId() {
        return userId;
    }

    public User setUserId(String userId) {
        this.userId = userId;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public User setPassword(String password) {
        this.password = password;
        return this;
    }

    public String getName() {
        return name;
    }

    public User setName(String name) {
        this.name = name;
        return this;
    }

    private boolean matchUserId(String userId) {
        return this.userId.equals(userId);
    }

    public void update(User loginUser, User target) {
        if (!matchUserId(loginUser.getUserId())) {
            throw new UnAuthorizedException();
        }

        if (!matchPassword(target.password)) {
            return;
        }

        this.name = target.name;
    }

    public boolean matchPassword(String password) {
        return this.password.equals(password);
    }

    public UserDto _toUserDto() {
        return new UserDto(this.userId, this.password, this.name);
    }

    @JsonIgnore
    public boolean isGuestUser() {
        return false;
    }

    private static class GuestUser extends User {
        @Override
        public boolean isGuestUser() {
            return true;
        }
    }

    @Override
    public String toString() {
        return "User [userId=" + userId + ", password=" + password + ", name=" + name + "]";
    }
}
