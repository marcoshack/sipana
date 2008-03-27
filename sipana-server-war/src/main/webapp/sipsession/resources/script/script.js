function selectDate(){

input = document.getElementsByTagName("input");

Calendar.setup({
        inputField     :    input[0],
        ifFormat       :    "%d/%m/%Y",
        button         :    "trigger_start",
        align          :    "Tl",
        singleClick    :    true
});   
Calendar.setup({
        inputField     :    input[1],
        ifFormat       :    "%d/%m/%Y",
        button         :    "trigger_end",
        align          :    "Tl",
        singleClick    :    true
});

}