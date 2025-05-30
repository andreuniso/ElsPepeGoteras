using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace RiskClient.Models
{
    public class RegionInfo
    {
        public int id { get; set; }
        public string nom { get; set; }
        public int reforcTropes { get; set; }
        public List<PaisInfo> paisos { get; set; }
    }
}
    


