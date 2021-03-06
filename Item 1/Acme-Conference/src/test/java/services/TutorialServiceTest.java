
package services;

import java.util.Date;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import security.LoginService;
import utilities.AbstractTest;
import domain.Conference;
import domain.Tutorial;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@Transactional
public class TutorialServiceTest extends AbstractTest {

	@Autowired
	TutorialService		tutorialService;

	@Autowired
	ConferenceService	conferenceService;

	@Autowired
	LoginService		loginService;


	@Test
	public void createTest() {
		super.authenticate("admin1");
		final Tutorial tutorial = this.tutorialService.create();
		super.unauthenticate();
	}

	@Test(expected = IllegalArgumentException.class)
	public void deleteWithConferenceNotInDraftModeTest() {
		super.authenticate("admin1");
		final int tutorialId = this.getEntityId("tutorial1");
		final Tutorial tutorial = this.tutorialService.findOne(tutorialId);
		final Conference conference = this.conferenceService.findConferenceByTutorialId(tutorial.getId());
		this.tutorialService.delete(tutorial, conference.getId());
		super.unauthenticate();
	}

	@Test
	public void deleteWithConferenceInDraftModeTest() {
		super.authenticate("admin1");
		final int tutorialId = this.getEntityId("tutorial1");
		final Tutorial tutorial = this.tutorialService.findOne(tutorialId);
		final Conference conference = this.conferenceService.findConferenceByTutorialId(tutorial.getId());
		final Date submission = new Date(116, 5, 3);
		final Date notification = new Date(117, 5, 3);
		final Date cameraReady = new Date(118, 5, 3);
		final Date startDate = new Date(119, 5, 3);
		final Date endDate = new Date(120, 5, 3);
		conference.setIsDraft(true);
		conference.setSubmission(submission);
		conference.setCameraReady(cameraReady);
		conference.setNotification(notification);
		conference.setStartDate(startDate);
		conference.setEndDate(endDate);
		final Conference saved = this.conferenceService.save(conference);
		this.tutorialService.delete(tutorial, saved.getId());
		super.unauthenticate();
	}
}
