function selectDate(){

input = document.getElementsByTagName("input");

Calendar.setup({
        inputField     :    input[0],
        ifFormat       :    "%d/%m/%Y %H:%M",
        button         :    "trigger_start",
        align          :    "Tl",
        showsTime      :    true,
        timeFormat     :    "24",
        singleClick    :    true
});   
Calendar.setup({
        inputField     :    input[1],
        ifFormat       :    "%d/%m/%Y %H:%M",
        button         :    "trigger_end",
        align          :    "Tl",
        showsTime      :    true,
        timeFormat     :    "24",
        singleClick    :    true
});

}