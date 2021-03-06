
package services;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import repositories.SectionRepository;
import repositories.TutorialRepository;
import domain.Activity;
import domain.Conference;
import domain.Section;
import domain.Tutorial;

@Service
@Transactional
public class SectionService {

	@Autowired
	private SectionRepository		sectionRepository;

	@Autowired
	private TutorialRepository		tutorialRepository;

	@Autowired
	private AdministratorService	administratorService;

	@Autowired
	private ConferenceService		conferenceService;


	//	@Autowired
	//	private ConferenceService		conferenceService;

	//METODOS CRUD
	public Section create() {
		this.administratorService.findByPrincipal();
		final Section res = new Section();
		res.setPictures(new ArrayList<String>());
		return res;
	}
	public Section findOne(final int idSection) {
		final Section res = this.sectionRepository.findOne(idSection);
		Assert.notNull(res, "La Section es nula - findOne");
		return res;
	}

	public Collection<Section> findAll() {
		final Collection<Section> res = this.sectionRepository.findAll();
		Assert.notNull(res, "La Collection<Section> es nula - findAll");
		return res;
	}
	public Section save(final Section section, final int tutorialId) {
		Assert.isTrue(tutorialId != 0);
		this.administratorService.findByPrincipal();
		Assert.notNull(section, "La section es nula - save");
		//	TODO: Decidir si section se anyade solo en draft mode o siempre
		//	final Conference c = this.conferenceService.findConference(tutorial.getId());
		//	Assert.isTrue(c.getIsDraft(), "La conferencia asociada al tutorial sobre el que escribimos la seccion tiene que estar en modo draft");
		final Tutorial tutorial = this.tutorialRepository.findOne(tutorialId);
		Assert.notNull(tutorial);
		final Conference c = this.conferenceService.findConferenceByTutorialId(tutorialId);
		Assert.isTrue(c.getIsDraft(), "conference.not.draft.error");
		final Collection<Section> sections = tutorial.getSections();
		final Section res = this.sectionRepository.save(section);
		if (section.getId() == 0) {
			sections.add(res);
			tutorial.setSections(sections);
			this.tutorialRepository.save(tutorial);
		} else {
			final boolean contained = sections.contains(res);
			Assert.isTrue(contained, "section.contains.error");
		}
		return res;
	}
	public Collection<Section> findByTutorial(final int tutorialId) {
		final Collection<Section> res = this.sectionRepository.findByTutorial(tutorialId);
		Assert.notNull(res);
		return res;
	}

	public void delete(final Section section, final int tutorialId, final int conferenceId) {
		this.administratorService.findByPrincipal();
		Assert.isTrue(this.sectionRepository.findOne(section.getId()).equals(section), "No se puede borrar una section que no existe");
		Assert.notNull(section);
		Assert.isTrue(section.getId() != 0);
		final Conference conference = this.conferenceService.findOne(conferenceId);
		Assert.isTrue(conference.getIsDraft(), "conference.not.draft.error");
		final Tutorial tutorial = this.tutorialRepository.findOne(tutorialId);
		Collection<Activity> acs = conference.getActivities();
		acs = this.conferenceService.findConferenceActivities(conferenceId);
		Assert.isTrue(acs.contains(tutorial), "La conferencia sobre la que se va a eliminar el tutorial debe de contener a este entre sus actividades");
		final Collection<Section> sections = tutorial.getSections();
		sections.remove(section);
		this.tutorialRepository.save(tutorial);
		this.sectionRepository.delete(section.getId());
	}

	void deleteAll(final Collection<Section> sections) {
		this.sectionRepository.deleteInBatch(sections);
	}

}
