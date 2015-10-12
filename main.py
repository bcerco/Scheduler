import kivy
from kivy.uix.gridlayout import GridLayout
from kivy.app import App
from kivy.uix.button import Button
from kivy.uix.label import Label
from kivy.graphics import Color,Rectangle

days = {1:"Mon", 2:"Tue", 3:"Wed", 4:"Thu", 5:"Fri", 6:"Sat"}
hours = {7:"8:00am", 14:"9:00am", 21:"10:00am", 28:"11:00am", 35:"12:00pm", 
        42:"1:00pm", 49:"2:00pm", 56:"3:00pm", 63:"4:00pm", 70:"5:00pm", 
        77:"6:00pm", 84:"7:00pm", 91:"8:00pm", 98:"9:00pm"}

def build_class(course,instructor, time_slot):
    ret = course +"\n" +instructor+"\n"+time_slot
    return ret

class Scheduler(App):
    def build(self):
        layout = GridLayout(cols=7, rows=15)
        for i in range(0,7*15):
            if i == 0:
                layout.add_widget(Label(text = ""))
            elif i > 0 and i < 7:
                name = days[i]
                button = Button(text = name)
                button.background_color = (1,1,1,0.5)
                layout.add_widget(button)
            elif i in hours.keys():
                name = hours[i]
                layout.add_widget(Label(text = name))
            elif i == 15 or i == 17 or i == 19:
                button = Button(text=build_class("CMPSC 122","Chang","9:00-9:50am"))
                button.background_color = (0,0,1,1)
                layout.add_widget(button)
            else:
                name = ""
                button = Button(text = name)
                button.background_color = (0,1,0,1)
                layout.add_widget(button)
        
        return layout

if __name__ == "__main__":
    Scheduler().run()
