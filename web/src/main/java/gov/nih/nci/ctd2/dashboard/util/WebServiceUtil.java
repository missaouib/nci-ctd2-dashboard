package gov.nih.nci.ctd2.dashboard.util;

import gov.nih.nci.ctd2.dashboard.dao.DashboardDao;
import gov.nih.nci.ctd2.dashboard.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

public class WebServiceUtil {
    @Autowired
    private DashboardDao dashboardDao;

    @Transactional
    @Cacheable(value = "entityCache")
    public List<? extends DashboardEntity> getDashboardEntities(String type, Integer filterBy) {
        List<? extends DashboardEntity> entities = new ArrayList<DashboardEntity>();
        if(type.equalsIgnoreCase("submission")) {
            if(filterBy != null) {
                SubmissionCenter submissionCenter = dashboardDao.getEntityById(SubmissionCenter.class, filterBy);
                if(submissionCenter != null) {
                    entities = dashboardDao.findSubmissionBySubmissionCenter(submissionCenter);
                }
            } else {
                entities = dashboardDao.findEntities(Submission.class);
            }
        } else if(type.equalsIgnoreCase("observation")) {
            if(filterBy != null) {
                Submission submission = dashboardDao.getEntityById(Submission.class, filterBy);
                if(submission != null) {
                    entities = dashboardDao.findObservationsBySubmission(submission);
                } else {
                    Subject subject = dashboardDao.getEntityById(Subject.class, filterBy);
                    if(subject != null) {
                        ArrayList<Observation> observations = new ArrayList<Observation>();
                        for (ObservedSubject observedSubject : dashboardDao.findObservedSubjectBySubject(subject)) {
                            observations.add(observedSubject.getObservation());
                        }
                        entities = observations;
                    }
                }
            } else {
                entities = dashboardDao.findEntities(Observation.class);
            }
        } else if(type.equals("center")) {
            entities = dashboardDao.findEntities(SubmissionCenter.class);
        } else if(type.equals("observedsubject") && filterBy != null) {
            Subject subject = dashboardDao.getEntityById(Subject.class, filterBy);
            if(subject != null) {
                entities = dashboardDao.findObservedSubjectBySubject(subject);
            } else {
                Observation observation = dashboardDao.getEntityById(Observation.class, filterBy);
                if(observation != null) {
                    entities = dashboardDao.findObservedSubjectByObservation(observation);
                }
            }
        } else if(type.equals("observedevidence") && filterBy != null) {
            Observation observation = dashboardDao.getEntityById(Observation.class, filterBy);
            if(observation != null) {
                entities = dashboardDao.findObservedEvidenceByObservation(observation);
            }
        } else if(type.equals("observationtemplate") && filterBy != null) {
            SubmissionCenter submissionCenter = dashboardDao.getEntityById(SubmissionCenter.class, filterBy);
            if(submissionCenter != null) {
                entities = dashboardDao.findObservationTemplateBySubmissionCenter(submissionCenter);
            }
        }
        return entities;
    }


}
