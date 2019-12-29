
// imports

#include "mediapipe/framework/calculator_framework.h"
#include "mediapipe/framework/port/status.h"
#include "mediapipe/framework/formats/image_frame.h"
#include "mediapipe/framework/port/ret_check.h"
#include <vector>

/*
node {
calculator:"ColorSliderCalculator"
input_stream: "Red"
input_stream: "Green"
input_stream: "Blue"
output_stream:"RGB_OUT:out_array"

}
*/


//calculator ColoSlider
namespace mediapipe {


class ColorSliderCalculator : public CalculatorBase {
public:

    ColorSliderCalculator() = default;
    ~ColorSliderCalculator() override = default;
    //ColorSliderCalculator() : initialized_(false){}

  static ::mediapipe::Status GetContract(CalculatorContract* cc);

  ::mediapipe::Status Open(CalculatorContext* cc) override;
  ::mediapipe::Status Process(CalculatorContext* cc) override;
  void make_array(int r,int g,int b,std::array<int,3>* out);
  };

  REGISTER_CALCULATOR(ColorSliderCalculator);

    //static
    ::mediapipe::Status ColorSliderCalculator::GetContract (CalculatorContract *cc){

    cc->Inputs().Index(0).Set<int>();
    cc->Inputs().Index(1).Set<int>();
    cc->Inputs().Index(2).Set<int>();

    if (cc->Outputs().HasTag("RGB_OUT")){
      cc->Outputs().Tag("RGB_OUT").Set<std::array<int,3>>();
    }

    return ::mediapipe::OkStatus();

    }

    ::mediapipe::Status ColorSliderCalculator::Open(CalculatorContext* cc) {
    cc->SetOffset(TimestampDiff(0));
    return ::mediapipe::OkStatus();
    }

    ::mediapipe::Status ColorSliderCalculator::Process(CalculatorContext* cc) {
    if (cc->Inputs().NumEntries() == 0) {
          return tool::StatusStop();
        }
    int red_buffer = cc->Inputs().Index(0).Value().Get<int>();
    int green_buffer = cc->Inputs().Index(1).Value().Get<int>();
    int blue_buffer = cc->Inputs().Index(2).Value().Get<int>();
    auto out = absl::make_unique<std::array<int,3>>();
    make_array(red_buffer,green_buffer,blue_buffer, out.get());


    // auto rgb = absl::make_unique<std::array<int, 3>>();
    // const auto out = {red_buffer, green_buffer,blue_buffer};

    cc->Outputs().Tag("RGB_OUT").Add(out.release(), cc->InputTimestamp());
    LOG(INFO) << "Color Slider Calculator Runner" << red_buffer << " " << green_buffer << " " << blue_buffer << "\n";
    return ::mediapipe::OkStatus();
    }




  void ColorSliderCalculator::make_array(int r,int g,int b,std::array<int,3>* out){
    (*out)[0] =r;
    (*out)[1] =g;
    (*out)[2] =b;
  }

} //end namespace
// REGISTER_CALCULATOR(::mediapipe::ColorSliderCalculator);
