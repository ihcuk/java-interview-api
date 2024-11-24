package com.talentreef.interviewquestions.takehome.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.talentreef.interviewquestions.takehome.models.Widget;
import com.talentreef.interviewquestions.takehome.respositories.WidgetRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Optional;

@RunWith(SpringRunner.class)
public class WidgetServiceTests {

  @Mock
  private WidgetRepository widgetRepository;

  @InjectMocks
  private WidgetService widgetService;

  // 1. Test for `getAllWidgets()`
  @Test
  public void when_getAllWidgets_expect_findAllResult() {
    Widget widget = Widget.builder().name("Widgette Nielson").build();
    List<Widget> response = List.of(widget);
    when(widgetRepository.findAll()).thenReturn(response);

    List<Widget> result = widgetService.getAllWidgets();

    assertThat(result).isEqualTo(response);
  }

  @Test
  public void when_getAllWidgets_emptyList_expect_emptyList() {
    when(widgetRepository.findAll()).thenReturn(List.of());

    List<Widget> result = widgetService.getAllWidgets();

    assertThat(result).isEmpty();
  }

  // 2. Test for `createWidgets()`
  @Test
  public void when_createWidgets_expect_saveAllResult() {
    Widget widget1 = Widget.builder().name("Widget1").description("Description1").price(10.99).build();
    Widget widget2 = Widget.builder().name("Widget2").description("Description2").price(20.99).build();
    List<Widget> widgets = List.of(widget1, widget2);
    when(widgetRepository.saveAll(any(List.class))).thenReturn(widgets);

    List<Widget> result = widgetService.createWidgets(widgets);

    assertThat(result).isEqualTo(widgets);
    verify(widgetRepository, times(1)).saveAll(widgets);
  }

  @Test
  public void when_createWidgets_emptyList_expect_saveAllCalled() {
    List<Widget> widgets = List.of();
    when(widgetRepository.saveAll(any(List.class))).thenReturn(widgets);

    List<Widget> result = widgetService.createWidgets(widgets);

    assertThat(result).isEmpty();
    verify(widgetRepository, times(1)).saveAll(widgets);
  }

  // 3. Test for `getWidgetByName()`
  @Test
  public void when_getWidgetByName_found_expect_widget() {
    String widgetName = "Widget1";
    Widget widget = Widget.builder().name(widgetName).description("Test Widget").price(12.99).build();
    when(widgetRepository.findById(widgetName)).thenReturn(Optional.of(widget));

    Optional<Widget> result = widgetService.getWidgetByName(widgetName);

    assertThat(result).isPresent();
    assertThat(result.get()).isEqualTo(widget);
  }

  @Test
  public void when_getWidgetByName_notFound_expect_empty() {
    String widgetName = "NonExistentWidget";
    when(widgetRepository.findById(widgetName)).thenReturn(Optional.empty());

    Optional<Widget> result = widgetService.getWidgetByName(widgetName);

    assertThat(result).isNotPresent();
  }

  // 4. Test for `updateWidget()`
  @Test
  public void when_updateWidget_found_expect_updatedWidget() {
    String widgetName = "Widget1";
    String newDescription = "Updated Description";
    Double newPrice = 15.99;
    Widget updatedWidget = Widget.builder().name(widgetName).description(newDescription).price(newPrice).build();
    when(widgetRepository.update(widgetName, newDescription, newPrice)).thenReturn(Optional.of(updatedWidget));

    Optional<Widget> result = widgetService.updateWidget(widgetName, newDescription, newPrice);

    assertThat(result).isPresent();
    assertThat(result.get()).isEqualTo(updatedWidget);
  }

  @Test
  public void when_updateWidget_notFound_expect_empty() {
    String widgetName = "NonExistentWidget";
    String newDescription = "Updated Description";
    Double newPrice = 15.99;
    when(widgetRepository.update(widgetName, newDescription, newPrice)).thenReturn(Optional.empty());

    Optional<Widget> result = widgetService.updateWidget(widgetName, newDescription, newPrice);

    assertThat(result).isNotPresent();
  }


  @Test
  public void when_deleteWidget_notFound_expect_false() {
    String widgetName = "NonExistentWidget";
    when(widgetRepository.deleteById(widgetName)).thenReturn(List.of());

    boolean result = widgetService.deleteWidget(widgetName);

    assertThat(result).isFalse();
    verify(widgetRepository, times(1)).deleteById(widgetName);
  }

  // Edge case: deleteWidget() with no widgets in the repository
  @Test
  public void when_deleteWidget_noWidgetsInRepo_expect_false() {
    String widgetName = "Widget1";
    when(widgetRepository.findAll()).thenReturn(List.of()); // No widgets in the repo
    when(widgetRepository.deleteById(widgetName)).thenReturn(List.of());

    boolean result = widgetService.deleteWidget(widgetName);

    assertThat(result).isFalse();
    verify(widgetRepository, times(1)).deleteById(widgetName);
  }
}
