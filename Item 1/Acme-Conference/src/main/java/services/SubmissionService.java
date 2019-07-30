
package services;

import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import repositories.SubmissionRepository;
import domain.Author;
import domain.Conference;
import domain.Paper;
import domain.Submission;

@Service
@Transactional
public class SubmissionService {

	@Autowired
	private SubmissionRepository	submissionRepository;

	@Autowired
	private AuthorService			authorService;

	@Autowired
	private AdministratorService	administratorService;

	@Autowired
	private ConferenceService		conferenceService;


	public Submission create(final int conferenceId) {
		final Submission res = new Submission();
		final String ticker = this.generateTicker();
		final Author principal = this.authorService.findByPrincipal();
		res.setAuthor(principal);
		res.setTicker(ticker);
		res.setMoment(new Date());
		res.setStatus("UNDER-REVIEWED");

		final Conference conference = this.conferenceService.findOne(conferenceId);
		Assert.isTrue(!conference.getIsDraft(), "conference must be in final mode");
		res.setConference(conference);

		return res;
	}

	/*
	 * public Submission save(final Submission submission, final Conference conference, final Author author, final Paper cameraReadyPaper, final Paper reviewPaper) {
	 * Assert.notNull(submission);
	 * Assert.notNull(conference);
	 * Assert.notNull(author);
	 * Assert.notNull(cameraReadyPaper);
	 * Assert.notNull(reviewPaper);
	 * 
	 * Assert.isTrue(Pattern.matches("yyyy-MM-dd HH:mm", submission.getMoment().toString()));
	 * Assert.isTrue(Pattern.matches("^([A-Z]{3}-[0-9A-Z]{4})$", submission.getTicker()));
	 * Assert.isTrue(Pattern.matches("^(UNDER-REVIEWED|ACCEPTED|REJECTED)$", submission.getStatus()));
	 * 
	 * submission.setConference(conference);
	 * submission.setAuthor(author);
	 * submission.setCameraReadyPaper(cameraReadyPaper);
	 * submission.setReviewPaper(reviewPaper);
	 * 
	 * final Submission res = this.submissionRepository.save(submission);
	 * Assert.notNull(res);
	 * 
	 * return res;
	 * }
	 */

	public Submission submits(final Submission submission, final Paper reviewPaper) {
		Submission res;
		final Author principal = this.authorService.findByPrincipal();
		submission.setAuthor(principal);
		Assert.isTrue(submission.getId() == 0);
		Assert.notNull(submission.getConference() != null);
		Assert.isTrue(submission.getStatus().equals("UNDER-REVIEWED"));
		final Date now = new Date();
		Assert.isTrue(now.before(submission.getConference().getSubmission()), "submission deadline is elapsed");
		submission.setMoment(new Date());
		submission.setReviewPaper(reviewPaper);
		res = this.submissionRepository.save(submission);
		Assert.notNull(res);
		return res;

	}
	public void sendCameraReadyPaper(final int submissionId, final Paper cameraReadyPaper) {
		final Author principal = this.authorService.findByPrincipal();
		final Submission retrieved = this.findOne(submissionId);
		Assert.notNull(retrieved);
		Assert.isTrue(retrieved.getAuthor().equals(principal));
		Assert.isTrue(retrieved.getStatus().equals("ACCEPTED"));
		final Date now = new Date();
		Assert.isTrue(now.before(retrieved.getConference().getCameraReady()), "camera-ready deadline is elapsed");
		retrieved.setCameraReadyPaper(cameraReadyPaper);
		this.submissionRepository.save(retrieved);

	}

	public void acceptSubmission(final int submissionId) {
		this.administratorService.findByPrincipal();
		final Submission retrieved = this.findOne(submissionId);
		Assert.notNull(retrieved);
		Assert.isTrue(retrieved.getStatus().equals("UNDER-REVIEWED"));
		retrieved.setStatus("ACCEPTED");
		this.submissionRepository.save(retrieved);
	}

	public void rejectSubmission(final int submissionId) {
		this.administratorService.findByPrincipal();
		final Submission retrieved = this.findOne(submissionId);
		Assert.notNull(retrieved);
		Assert.isTrue(retrieved.getStatus().equals("UNDER-REVIEWED"));
		retrieved.setStatus("REJECTED");
		this.submissionRepository.save(retrieved);
	}

	/**
	 * Run a decision-making procedure on a conference,
	 * as long as the corresponding submission deadline has elapsed.
	 * Making a decision on a submission means analysing the reports
	 * written by the reviewers to decide if the corresponding submission
	 * must change its status to either REJECTED or ACCEPTED.
	 * A submission is accepted if the number of reports with decision
	 * ACCEPT that it's got is greater than or equal to the number
	 * of reports with decision REJECT that it's got;
	 * in cases of ties, reports whose decision is BORDER-LINE
	 * are considered to ACCEPT the paper; in cases in which ties persist,
	 * then the corresponding submissions are accepted.
	 **/

	public void decideOnSubmission(final int submissionId) {

	}

	public Submission findOne(final Integer submissionId) {
		Assert.notNull(submissionId);
		final Submission res = this.submissionRepository.findOne(submissionId);
		Assert.notNull(res);

		return res;
	}

	public Collection<Submission> findAll() {
		final Collection<Submission> res = this.submissionRepository.findAll();
		Assert.notNull(res);

		return res;
	}

	public List<Submission> getSubmissionsByAuthor(final int authorId) {
		final List<Submission> res = this.submissionRepository.getSubmissionsByAuthor(authorId);
		Assert.notNull(res);
		return res;
	}

	public Collection<Submission> findSubmissionsByConference(final int conferenceId) {
		final Collection<Submission> result = this.submissionRepository.findSubmissionsByConference(conferenceId);
		Assert.notNull(result);
		return result;
	}

	public void delete(final Integer submissionId) {
		Assert.notNull(submissionId);
		this.submissionRepository.delete(submissionId);
	}

	/** El ticker es ABC-XXXX donde ABC son las inciales del autor y XXXX cuatro letras randoms en mayuscula **/
	private String generateTicker() {
		String res = "";
		final String abecedary = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
		final Integer n1 = (int) Math.floor(Math.random() * 25 + 1);
		final Integer n2 = (int) Math.floor(Math.random() * 25 + 1);
		final Integer n3 = (int) Math.floor(Math.random() * 25 + 1);
		final Integer n4 = (int) Math.floor(Math.random() * 25 + 1);

		final Author author = this.authorService.findByPrincipal();

		final Character initial1 = author.getName().charAt(0);
		Character initial2 = null;
		Character initial3 = null;
		if (author.getSurname().size() == 1) {
			initial2 = author.getSurname().get(0).charAt(0);
			initial3 = 'X';
		} else {
			initial2 = author.getSurname().get(0).charAt(0);
			initial3 = author.getSurname().get(1).charAt(0);
		}

		res = initial1 + initial2 + initial3 + "-" + abecedary.charAt(n1) + abecedary.charAt(n2) + abecedary.charAt(n3) + abecedary.charAt(n4);

		final Collection<Submission> submissions = this.submissionRepository.getSubmissionWithTicker(res);
		if (!submissions.isEmpty())
			this.generateTicker();
		return res;
	}
}
