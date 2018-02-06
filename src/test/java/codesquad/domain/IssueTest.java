package codesquad.domain;

import codesquad.UnAuthorizedException;
import codesquad.dto.IssueDto;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.*;

public class IssueTest {
	public static final User JAVAJIGI = new User(1L, "javajigi", "testtest", "자바지기");
	public static final User SARAM = new User(2L, "saram4030", "testtest", "이끼룩");

	public static Issue newIssue(long id, User writer) {
		return new Issue(id, "issueSubject" + id, "issueComment" + id, writer);
	}

	public static IssueDto newIssueDto(long id) {
		return new IssueDto("issueSubject" + id, "issueComment" + id);
	}

	@Test
	public void update_owner() throws Exception {
		Issue origin = newIssue(1L, SARAM);
		IssueDto target = newIssueDto(2L);
		origin.update(SARAM, target);

		assertThat(origin.getSubject(), is(target.getSubject()));
	}

	@Test(expected = UnAuthorizedException.class)
	public void update_not_owner() throws Exception {
		Issue origin = newIssue(1L, SARAM);
		IssueDto target = newIssueDto(2L);
		origin.update(JAVAJIGI, target);

		assertThat(origin.getSubject(), is(target.getSubject()));
	}

	@Test
	public void match_writer() throws Exception {
		Issue issue = newIssue(1L, SARAM);

		assertTrue(issue.isOwner(SARAM));
	}

	@Test
	public void mismatch_writer() throws Exception {
		Issue issue = newIssue(1L, SARAM);

		assertFalse(issue.isOwner(JAVAJIGI));
	}

}