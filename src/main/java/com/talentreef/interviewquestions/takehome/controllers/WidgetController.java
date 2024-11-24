package com.talentreef.interviewquestions.takehome.controllers;

import com.talentreef.interviewquestions.takehome.models.Widget;
import com.talentreef.interviewquestions.takehome.services.WidgetService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.Assert;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.CrossOrigin;


import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping(value= "/v1/widgets", produces = MediaType.APPLICATION_JSON_VALUE)
@CrossOrigin(origins = "http://localhost:3000")
@Validated
public class WidgetController {

  private final WidgetService widgetService;

  public WidgetController(WidgetService widgetService) {
    Assert.notNull(widgetService, "widgetService must not be null");
    this.widgetService = widgetService;
  }

  @PostMapping
  public ResponseEntity<List<Widget>> createWidgets(@Valid @RequestBody Widget widget) {
    return createWidgets(List.of(widget));
  }

  @PostMapping("/bulk")
  public ResponseEntity<List<Widget>> createWidgets(@Valid @RequestBody List<Widget> widgetList) {
    try {
      List<Widget> createdWidgets = widgetService.createWidgets(widgetList);
      return new ResponseEntity<>(createdWidgets, HttpStatus.CREATED);
    } catch (Exception e) {
      log.error("Error creating widgets: {}", e.getMessage());
      return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @GetMapping
  public ResponseEntity<List<Widget>> getAllWidgets() {
    try {
      List<Widget> widgets = widgetService.getAllWidgets();
      return new ResponseEntity<>(widgets, HttpStatus.OK);
    } catch (Exception e) {
      log.error("Error fetching all widgets: {}", e.getMessage());
      return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }


  @GetMapping("/{name}")
  public ResponseEntity<Widget> getWidgetByName(@PathVariable String name) {
    try {
      Optional<Widget> widget = widgetService.getWidgetByName(name);
      return widget.map(ResponseEntity::ok)
              .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    } catch (Exception e) {
      log.error("Error fetching widget by name: {}", e.getMessage());
      return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @PutMapping("/{name}")
  public ResponseEntity<Widget> updateWidget(@PathVariable String name,
                                             @RequestParam(required = false) String description,
                                             @RequestParam(required = false) Double price) {
    try {
      Optional<Widget> updatedWidget = widgetService.updateWidget(name, description, price);
      return updatedWidget.map(ResponseEntity::ok)
              .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    } catch (Exception e) {
      log.error("Error updating widget: {}", e.getMessage());
      return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @DeleteMapping("/{name}")
  public ResponseEntity<Void> deleteWidget(@PathVariable String name) {
    try {
      boolean isDeleted = widgetService.deleteWidget(name);
      if (isDeleted) {
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);  // Successful deletion
      } else {
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);  // Widget not found
      }
    } catch (Exception e) {
      log.error("Error deleting widget: {}", e.getMessage());
      return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }
}
