
package controllers;

import java.util.Collection;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.ActorService;
import services.AdministratorService;
import services.ConferenceService;
import services.TutorialService;
import domain.Tutorial;

@Controller
@RequestMapping("/tutorial")
public class TutorialController extends AbstractController {

	@Autowired
	private TutorialService			tutorialService;

	@Autowired
	private AdministratorService	administratorService;

	@Autowired
	private ActorService			actorService;

	@Autowired
	private ConferenceService		conferenceService;


	// CREATE  ---------------------------------------------------------------		

	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create(@RequestParam final int conferenceId, final String fromConferenceDisplay) {
		ModelAndView result;
		final Tutorial tutorial = this.tutorialService.create();
		result = this.createEditModelAndView(tutorial, conferenceId);
		result.addObject("fromConferenceDisplay", fromConferenceDisplay);
		return result;
	}
	// LIST  ---------------------------------------------------------------		

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list(@RequestParam final int conferenceId) {
		ModelAndView result;
		result = new ModelAndView("tutorial/list");

		final Collection<Tutorial> tutorials = this.tutorialService.findTutorialsByConference(conferenceId);
		result.addObject("tutorials", tutorials);
		result.addObject("conferenceId", conferenceId);
		final boolean isDraft = this.conferenceService.findOne(conferenceId).getIsDraft();
		result.addObject("isDraft", isDraft);

		return result;
	}
	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public ModelAndView display(@RequestParam final int tutorialId, @RequestParam final int conferenceId) {

		final ModelAndView result;
		final Tutorial tutorial = this.tutorialService.findOne(tutorialId);
		result = new ModelAndView("tutorial/display");
		result.addObject("tutorial", tutorial);
		result.addObject("conferenceId", conferenceId);
		final boolean isDraft = this.conferenceService.findOne(conferenceId).getIsDraft();
		result.addObject("isDraft", isDraft);

		return result;
	}

	// UPDATE  ---------------------------------------------------------------		

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam final int tutorialId, @RequestParam final int conferenceId) {
		ModelAndView result;
		Tutorial tutorial;

		tutorial = this.tutorialService.findOne(tutorialId);

		if (tutorial != null)
			result = this.createEditModelAndView(tutorial, conferenceId);
		else
			result = new ModelAndView("redirect:/misc/403.jsp");

		return result;
	}

	// SAVE  ---------------------------------------------------------------		

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(@Valid final Tutorial tutorial, final BindingResult binding, final HttpServletRequest request) {
		ModelAndView result;
		String paramTutorialId;
		Integer conferenceId;

		paramTutorialId = request.getParameter("conferenceId");
		conferenceId = paramTutorialId.isEmpty() ? null : Integer.parseInt(paramTutorialId);

		if (binding.hasErrors())
			result = this.createEditModelAndView(tutorial, conferenceId);
		else
			try {
				this.tutorialService.save(tutorial, conferenceId);
				result = this.list(conferenceId);
			} catch (final Throwable e) {
				result = this.createEditModelAndView(tutorial, conferenceId, "tutorial.commit.error");
			}
		return result;
	}

	// DELETE -----------------------------------------------------------

	@RequestMapping(value = "/delete", method = RequestMethod.GET)
	public ModelAndView delete(@RequestParam final int conferenceId, @RequestParam final int tutorialId) {

		ModelAndView res;
		final Tutorial toDelete = this.tutorialService.findOne(tutorialId);

		try {
			this.tutorialService.delete(toDelete, conferenceId);
			res = this.list(conferenceId);
		} catch (final Throwable oops) {
			res = this.display(tutorialId, conferenceId);
			final String error = "delete.error";
			res.addObject("error", error);
		}
		return res;
	}

	// CREATEEDITMODELANDVIEW -----------------------------------------------------------

	protected ModelAndView createEditModelAndView(final Tutorial tutorial, final int conferenceId) {
		return this.createEditModelAndView(tutorial, conferenceId, null);
	}

	protected ModelAndView createEditModelAndView(final Tutorial tutorial, final int conferenceId, final String messageCode) {
		final ModelAndView result;

		this.administratorService.findByPrincipal();

		result = new ModelAndView("tutorial/edit");
		result.addObject("tutorial", tutorial);
		result.addObject("message", messageCode);
		result.addObject("actors", this.actorService.findAll());
		result.addObject("conferenceId", conferenceId);

		return result;
	}

}
