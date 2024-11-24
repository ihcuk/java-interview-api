package com.talentreef.interviewquestions.takehome.respositories;

import com.talentreef.interviewquestions.takehome.models.Widget;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class WidgetRepository {

  private List<Widget> table = new ArrayList<>();

  // Delete widget by name
  public List<Widget> deleteById(String name) {
    this.table = table.stream()
            .filter(widget -> !name.equals(widget.getName()))
            .collect(Collectors.toList());
    return table;
  }

  // List all widgets
  public List<Widget> findAll() {
    return table;
  }

  // Save a single widget
  public Widget save(Widget widget) {
    // Remove any existing widget with the same name
    deleteById(widget.getName());
    // Add the new widget
    table.add(widget);
    return widget;
  }

  // List all widgets (duplicate of findAll, so this is not necessary)
  public List<Widget> getAll() {
    return table;
  }

  // Find widget by name
  public Optional<Widget> findById(String name) {
    return table.stream()
            .filter(widget -> name.equals(widget.getName()))
            .findAny();
  }

  // Save multiple widgets
  public List<Widget> saveAll(List<Widget> widgetsList) {
    // Iterate over each widget in the provided list
    widgetsList.forEach(widget -> {
      // Remove any existing widget with the same name
      deleteById(widget.getName());
      // Add the new widget
      table.add(widget);
    });
    return widgetsList;  // Return the list of saved widgets
  }

  // Update widget's description or price
  public Optional<Widget> update(String name, String description, Double price) {
    Optional<Widget> widgetOpt = findById(name);

    // If the widget exists, update its description or price
    if (widgetOpt.isPresent()) {
      Widget widget = widgetOpt.get();

      if (description != null && !description.isEmpty()) {
        widget.setDescription(description);  // Update description if provided
      }

      if (price != null && price > 0) {
        widget.setPrice(price);  // Update price if provided
      }

      // Optionally, we could update the widget directly in the table (in this case, it will already be updated)
      return Optional.of(widget);  // Return the updated widget
    }

    // If widget not found, return empty Optional
    return Optional.empty();
  }

}
