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

  // 1. Retrieve all widgets
  public List<Widget> getAllWidgets() {
    return widgetRepository.findAll();
  }

  // 2. Create new widgets
  public List<Widget> createWidgets(List<Widget> widgets) {
    return widgetRepository.saveAll(widgets);
  }

  // 3. Retrieve a widget by name
  public Optional<Widget> getWidgetByName(String name) {
    return widgetRepository.findById(name);
  }

  // 4. Update a widget's description or price
  public Optional<Widget> updateWidget(String name, String description, Double price) {
    return widgetRepository.update(name, description, price);
  }

  // 5. Delete a widget by name
  public boolean deleteWidget(String name) {
    List<Widget> remainingWidgets = widgetRepository.deleteById(name);
    // Return true if the widget list size is smaller after deletion
    return remainingWidgets.size() < widgetRepository.findAll().size();
  }

}
