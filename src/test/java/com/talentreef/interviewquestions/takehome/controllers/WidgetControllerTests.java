package com.talentreef.interviewquestions.takehome.controllers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.talentreef.interviewquestions.takehome.models.Widget;
import com.talentreef.interviewquestions.takehome.services.WidgetService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;
import java.util.Optional;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class WidgetControllerTests {

  private static final String BASE_URL = "/v1/widgets";
  final private ObjectMapper objectMapper = new ObjectMapper();

  private MockMvc mockMvc;

  @Mock
  private WidgetService widgetService;

  @InjectMocks
  private WidgetController widgetController;

  @Before
  public void init() {
    mockMvc = MockMvcBuilders.standaloneSetup(widgetController).build();
  }

  // 1. Test for `GET /v1/widgets`
  @Test
  public void when_getAllWidgets_expect_allWidgets() throws Exception {
    Widget widget = Widget.builder().name("Widget von Hammersmark").build();
    List<Widget> allWidgets = List.of(widget);
    when(widgetService.getAllWidgets()).thenReturn(allWidgets);

    MvcResult result = mockMvc.perform(get(BASE_URL))
            .andExpect(status().isOk())
            .andDo(print())
            .andReturn();

    List<Widget> parsedResult = objectMapper.readValue(result.getResponse().getContentAsString(),
            new TypeReference<List<Widget>>(){});
    assertThat(parsedResult).isEqualTo(allWidgets);
  }

  @Test
  public void when_getAllWidgets_emptyList_expect_emptyList() throws Exception {
    when(widgetService.getAllWidgets()).thenReturn(List.of());

    MvcResult result = mockMvc.perform(get(BASE_URL))
            .andExpect(status().isOk())
            .andDo(print())
            .andReturn();

    List<Widget> parsedResult = objectMapper.readValue(result.getResponse().getContentAsString(),
            new TypeReference<List<Widget>>(){});
    assertThat(parsedResult).isEmpty();
  }

  @Test
  public void when_getAllWidgets_serviceThrowsException_expect_500() throws Exception {
    when(widgetService.getAllWidgets()).thenThrow(new RuntimeException("Service Error"));

    mockMvc.perform(get(BASE_URL))
            .andExpect(status().isInternalServerError())
            .andDo(print());
  }

  // 2. Test for `POST /v1/widgets`
  @Test
  public void when_createWidgets_expect_createdWidgets() throws Exception {
    Widget widget1 = Widget.builder().name("Widget1").description("Description1").price(10.99).build();
    Widget widget2 = Widget.builder().name("Widget2").description("Description2").price(20.99).build();
    List<Widget> widgets = List.of(widget1, widget2);
    when(widgetService.createWidgets(any(List.class))).thenReturn(widgets);

    MvcResult result = mockMvc.perform(post(BASE_URL)
                    .contentType("application/json")
                    .content(objectMapper.writeValueAsString(widgets)))
            .andExpect(status().isCreated())
            .andDo(print())
            .andReturn();

    List<Widget> createdWidgets = objectMapper.readValue(result.getResponse().getContentAsString(),
            new TypeReference<List<Widget>>(){});
    assertThat(createdWidgets).isEqualTo(widgets);
  }

  @Test
  public void when_createWidgets_serviceThrowsException_expect_500() throws Exception {
    when(widgetService.createWidgets(any(List.class))).thenThrow(new RuntimeException("Service Error"));

    mockMvc.perform(post(BASE_URL)
                    .contentType("application/json")
                    .content(objectMapper.writeValueAsString(List.of())))
            .andExpect(status().isInternalServerError())
            .andDo(print());
  }

  // 3. Test for `GET /v1/widgets/{name}`
  @Test
  public void when_getWidgetByName_expect_widget() throws Exception {
    String widgetName = "Widget1";
    Widget widget = Widget.builder().name(widgetName).description("Test Widget").price(12.99).build();
    when(widgetService.getWidgetByName(widgetName)).thenReturn(Optional.of(widget));

    MvcResult result = mockMvc.perform(get(BASE_URL + "/" + widgetName))
            .andExpect(status().isOk())
            .andDo(print())
            .andReturn();

    Widget returnedWidget = objectMapper.readValue(result.getResponse().getContentAsString(), Widget.class);
    assertThat(returnedWidget).isEqualTo(widget);
  }

  @Test
  public void when_getWidgetByName_notFound_expect_404() throws Exception {
    String widgetName = "NonExistentWidget";
    when(widgetService.getWidgetByName(widgetName)).thenReturn(Optional.empty());

    mockMvc.perform(get(BASE_URL + "/" + widgetName))
            .andExpect(status().isNotFound())
            .andDo(print());
  }

  @Test
  public void when_getWidgetByName_serviceThrowsException_expect_500() throws Exception {
    String widgetName = "Widget1";
    when(widgetService.getWidgetByName(widgetName)).thenThrow(new RuntimeException("Service Error"));

    mockMvc.perform(get(BASE_URL + "/" + widgetName))
            .andExpect(status().isInternalServerError())
            .andDo(print());
  }

  // 4. Test for `PUT /v1/widgets/{name}`
  @Test
  public void when_updateWidget_expect_updatedWidget() throws Exception {
    String widgetName = "Widget1";
    Widget updatedWidget = Widget.builder().name(widgetName).description("Updated Description").price(15.99).build();
    when(widgetService.updateWidget(any(String.class), any(String.class), any(Double.class)))
            .thenReturn(Optional.of(updatedWidget));

    MvcResult result = mockMvc.perform(put(BASE_URL + "/" + widgetName)
                    .param("description", "Updated Description")
                    .param("price", "15.99"))
            .andExpect(status().isOk())
            .andDo(print())
            .andReturn();

    Widget returnedWidget = objectMapper.readValue(result.getResponse().getContentAsString(), Widget.class);
    assertThat(returnedWidget).isEqualTo(updatedWidget);
  }

  @Test
  public void when_updateWidget_notFound_expect_404() throws Exception {
    String widgetName = "NonExistentWidget";
    when(widgetService.updateWidget(any(String.class), any(String.class), any(Double.class)))
            .thenReturn(Optional.empty());

    mockMvc.perform(put(BASE_URL + "/" + widgetName)
                    .param("description", "Updated Description")
                    .param("price", "15.99"))
            .andExpect(status().isNotFound())
            .andDo(print());
  }

  @Test
  public void when_updateWidget_serviceThrowsException_expect_500() throws Exception {
    String widgetName = "Widget1";
    when(widgetService.updateWidget(any(String.class), any(String.class), any(Double.class)))
            .thenThrow(new RuntimeException("Service Error"));

    mockMvc.perform(put(BASE_URL + "/" + widgetName)
                    .param("description", "Updated Description")
                    .param("price", "15.99"))
            .andExpect(status().isInternalServerError())
            .andDo(print());
  }

  // 5. Test for `DELETE /v1/widgets/{name}`
  @Test
  public void when_deleteWidget_expect_204NoContent() throws Exception {
    String widgetName = "Widget1";
    when(widgetService.deleteWidget(widgetName)).thenReturn(true);

    mockMvc.perform(delete(BASE_URL + "/" + widgetName))
            .andExpect(status().isNoContent())
            .andDo(print());
  }

  @Test
  public void when_deleteWidget_notFound_expect_404() throws Exception {
    String widgetName = "NonExistentWidget";
    when(widgetService.deleteWidget(widgetName)).thenReturn(false);

    mockMvc.perform(delete(BASE_URL + "/" + widgetName))
            .andExpect(status().isNotFound())
            .andDo(print());
  }

  @Test
  public void when_deleteWidget_serviceThrowsException_expect_500() throws Exception {
    String widgetName = "Widget1";
    when(widgetService.deleteWidget(widgetName)).thenThrow(new RuntimeException("Service Error"));

    mockMvc.perform(delete(BASE_URL + "/" + widgetName))
            .andExpect(status().isInternalServerError())
            .andDo(print());
  }
}
