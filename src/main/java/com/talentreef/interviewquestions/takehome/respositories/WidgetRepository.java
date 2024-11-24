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

  public List<Widget> deleteById(String name) {
    this.table = table.stream()
            .filter(widget -> !name.equals(widget.getName()))
            .collect(Collectors.toList());
    return table;
  }

  public List<Widget> findAll() {
    return table;
  }

  public Widget save(Widget widget) {
    deleteById(widget.getName());
    table.add(widget);
    return widget;
  }

  public List<Widget> getAll() {
    return table;
  }

  public Optional<Widget> findById(String name) {
    return table.stream()
            .filter(widget -> name.equals(widget.getName()))
            .findAny();
  }

  public List<Widget> saveAll(List<Widget> widgetsList) {
    widgetsList.forEach(widget -> {
      deleteById(widget.getName());
      table.add(widget);
    });
    return widgetsList;
  }

  public Optional<Widget> update(String name, String description, Double price) {
    Optional<Widget> widgetOpt = findById(name);

    if (widgetOpt.isPresent()) {
      Widget widget = widgetOpt.get();

      if (description != null && !description.isEmpty()) {
        widget.setDescription(description);
      }

      if (price != null && price > 0) {
        widget.setPrice(price);
      }
      return Optional.of(widget);
    }
    return Optional.empty();
  }

}
