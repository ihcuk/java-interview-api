package com.talentreef.interviewquestions.takehome.services;

import com.talentreef.interviewquestions.takehome.models.Widget;
import com.talentreef.interviewquestions.takehome.respositories.WidgetRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class WidgetService {

  private final WidgetRepository widgetRepository;

  @Autowired
  private WidgetService(WidgetRepository widgetRepository) {
    Assert.notNull(widgetRepository, "widgetRepository must not be null");
    this.widgetRepository = widgetRepository;
  }

  public List<Widget> getAllWidgets() {
    return widgetRepository.findAll();
  }

  public List<Widget> createWidgets(List<Widget> widgets) {
    return widgetRepository.saveAll(widgets);
  }

  public Optional<Widget> getWidgetByName(String name) {
    return widgetRepository.findById(name);
  }

  public Optional<Widget> updateWidget(String name, String description, Double price) {
    return widgetRepository.update(name, description, price);
  }

  public boolean deleteWidget(String name) {
    List<Widget> remainingWidgets = widgetRepository.deleteById(name);
    return remainingWidgets.size() < widgetRepository.findAll().size();
  }

}
