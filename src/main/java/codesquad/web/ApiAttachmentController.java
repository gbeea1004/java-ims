package codesquad.web;

import codesquad.domain.issue.answer.Answer;
import codesquad.domain.issue.answer.Attachment;
import codesquad.domain.user.User;
import codesquad.security.LoginUser;
import codesquad.service.AnswerService;
import codesquad.service.IssueFileService;
import codesquad.service.IssueService;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.slf4j.LoggerFactory.getLogger;

@RestController
@RequestMapping("/api/issues/{issueId}/attachments")
public class ApiAttachmentController {
    private static final Logger log = getLogger(ApiAttachmentController.class);

    @Autowired
    private IssueFileService issueFileService;

    @Autowired
    private AnswerService answerService;

    @Autowired
    private IssueService issueService;

    @PostMapping("")
    public ResponseEntity<Answer> upload(@LoginUser User loginUser, MultipartFile file, @PathVariable long issueId) throws IOException {
        log.debug("upload");
        log.debug("original file name : {}", file.getOriginalFilename());
        log.debug("contentType : {}", file.getContentType());
        log.debug(String.valueOf(file.getBytes()));

        Attachment uploadFile = issueFileService.upload(loginUser, file);
        Answer answer = answerService.addAttachment(loginUser, issueService.findById(issueId), uploadFile);
        return new ResponseEntity<>(answer, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<FileSystemResource> download(@PathVariable long id) throws IOException {
        log.debug("download");
        Attachment file = issueFileService.findById(id);
        Path path = Paths.get(file.getSavedFileName());
        FileSystemResource resource = new FileSystemResource(path);

        HttpHeaders header = new HttpHeaders();
        header.setContentType(MediaType.MULTIPART_FORM_DATA);
        String encoredFilename = URLEncoder.encode(file.getOriginalFileName(), "UTF-8").replace("+", "%20");
        header.set(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + encoredFilename + ";filename*= UTF-8" + encoredFilename);
        header.setContentLength(resource.contentLength());
        return new ResponseEntity<>(resource, header, HttpStatus.OK);
    }
}
