package codesquad.web;

import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import codesquad.domain.Answer;
import support.test.AcceptanceTest;

public class ApiAnswerAcceptanceTest extends AcceptanceTest{
	private static final Logger log = LoggerFactory.getLogger(ApiAnswerAcceptanceTest.class);

		// o TODO Answer 생성 수정 삭제 만들기 - domain, repository, service
		//   TODO api controller 모자른거 추가
		//   TODO ajax를 이용해 답변 기능 먼저 만들기
		//   TODO milestone, label, assignee 적용
		
	@Test
	public void create() throws Exception {
		Answer answer = new Answer("create contents");
		String location = createResource("/api/answers", answer);
		
		Answer DBAnswer = getResource(location, Answer.class, findDefaultUser());
		assertTrue(DBAnswer.getContents().equals(answer.getContents()));
	}
	
	@Test
	public void update() throws Exception {
		Answer answer = new Answer("update 전 contents");
		String location = createResource("/api/answers", answer);
		
		Answer updateAnswer = new Answer("update 후 contents");
		basicAuthTemplate().put(location, updateAnswer);
		
		Answer DBAnswer = getResource(location, Answer.class, findDefaultUser());
		log.info("updateanswer : {}", updateAnswer.getContents());
		log.info("dbanswer : {}", DBAnswer.getContents());

		assertTrue(DBAnswer.getContents().equals(updateAnswer.getContents()));
	}
	
	@Test
	public void delete() throws Exception {
		Answer answer = new Answer("delete contents");
		String location = createResource("/api/answers", answer);
		basicAuthTemplate().delete(location);
		
		Answer DBAnswer = getResource(location, Answer.class, findDefaultUser());
		assertTrue(DBAnswer.isDeleted());
	}

}