using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Media;

namespace RiskClient.Models
{
    public class PaisInfo
    {
        public int id { get; set; }
        public string nom { get; set; }
        public List<int> fronteres { get; set; }
    }

}
